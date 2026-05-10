package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.order.dto.command.OrderCommand;
import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;
import kr.hhplus.be.server.domain.order.entity.Order;

import kr.hhplus.be.server.domain.order.entity.OrderDetail;
import kr.hhplus.be.server.domain.order.event.OrderEvent;
import kr.hhplus.be.server.domain.order.event.OrderEventPublisher;
import kr.hhplus.be.server.domain.order.repository.OrderDetailRepository;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderEventPublisher orderEventPublisher;

    @Transactional
    public OrderInfo.Create order(OrderCommand.Create orderCommand) {
        if(orderCommand.details().isEmpty()) {
            throw new CustomException(ErrorCode.ORDER_DETAILS_NOT_EXISTS);
        }

        Order order = orderRepository.save(orderCommand.toEntity());

        List<OrderDetail> orderDetails = orderCommand.details().stream()
                .map(orderDetail -> OrderDetail.create(
                        order,
                        orderDetail.productId(),
                        orderDetail.quantity(),
                        orderDetail.price()
                ))
                .toList();

        orderDetailRepository.saveAll(orderDetails);

        return OrderInfo.Create.of(order);
    }

    @Transactional(readOnly = true)
    public OrderInfo.Detail getOrderWithLock(String orderNo) {
        Order order = orderRepository.findByOrderNoWithLock(orderNo);
        if(order == null) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }

        return OrderInfo.Detail.of(order);
    }

    @Transactional(readOnly = true)
    public OrderInfo.Payment getOrderForPayment(String orderNo) {
        Order order = orderRepository.findByOrderNoWithLock(orderNo);
        if(order == null) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }
        order.validateCanPay();

        return OrderInfo.Payment.of(order);
    }

    @Transactional
    public OrderInfo.Confirm confirm(String orderNo) {
        Order order = orderRepository.findByOrderNoWithLock(orderNo);
        if(order == null) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }

        order.confirm();

        orderEventPublisher.publish(OrderEvent.Confirmed.of(order));

        return OrderInfo.Confirm.of(order);
    }

    @Transactional(readOnly = true)
    public OrderInfo.Confirm getOrder(String orderNo) {
        Order order = orderRepository.getOrder(orderNo);
        return OrderInfo.Confirm.of(order);
    }

    @Transactional
    public void failed(String orderNo) {
        Order order = orderRepository.findByOrderNoWithLock(orderNo);
        if(order == null) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }

        order.failed();
    }
}
