package kr.hhplus.be.server.domain.order.service;

import kr.hhplus.be.server.domain.order.dto.command.OrderCommand;
import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;
import kr.hhplus.be.server.domain.order.entity.Order;

import kr.hhplus.be.server.domain.order.entity.OrderDetail;
import kr.hhplus.be.server.domain.order.enums.OrderStatus;
import kr.hhplus.be.server.domain.order.repository.OrderDetailRepository;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;

    public OrderInfo.OrderDto order(OrderCommand.Order orderCommand) {
        if(orderCommand.details().isEmpty()) {
            throw new CustomException(ErrorCode.ORDER_DETAILS_NOT_EXISTS);
        }

        BigDecimal totalOriginalAmt = orderCommand.details().stream()
                .map(detail -> new BigDecimal(detail.quantity()).multiply(detail.price()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = orderRepository.save(Order.create(orderCommand.userId(), totalOriginalAmt));

        List<OrderDetail> orderDetails = orderCommand.details().stream()
                .map(orderDetail -> OrderDetail.create(
                        order,
                        orderDetail.productId(),
                        orderDetail.quantity(),
                        orderDetail.price()
                ))
                .toList();

        orderDetailRepository.saveAll(orderDetails);

        return OrderInfo.OrderDto.of(order);
    }

    public OrderInfo.OrderDto getOrderWithLock(Long orderId) {
        Order order = orderRepository.findByIdWithLock(orderId);
        if(order == null) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }

        return OrderInfo.OrderDto.of(order);
    }

    public void completePayment(Long orderId) {
        Order order = orderRepository.findByIdWithLock(orderId);
        if(order == null) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }

        if(order.getStatus().name().equals(OrderStatus.PAID.name())) {
            throw new CustomException(ErrorCode.ALREADY_PAID_ORDER);
        }

        order.completePayment();
    }
}
