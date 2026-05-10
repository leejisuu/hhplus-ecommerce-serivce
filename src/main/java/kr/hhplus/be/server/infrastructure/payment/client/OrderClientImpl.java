package kr.hhplus.be.server.infrastructure.payment.client;

import kr.hhplus.be.server.application.payment.client.OrderClient;
import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;
import kr.hhplus.be.server.domain.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderClientImpl implements OrderClient {

    private final OrderService orderService;

    @Override
    public OrderDto getOrder(String orderNo) {
        OrderInfo.Payment order = orderService.getOrderForPayment(orderNo);
        return new OrderDto(order.orderNo(), order.paymentAmt());
    }
}
