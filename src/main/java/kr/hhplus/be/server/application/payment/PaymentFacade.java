package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.payment.client.OrderClient;
import kr.hhplus.be.server.application.payment.dto.result.PaymentResult;
import kr.hhplus.be.server.domain.payment.dto.info.PaymentInfo;
import kr.hhplus.be.server.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentFacade {

    private final OrderClient orderClient;
    private final PaymentService paymentService;

    public PaymentResult.Payment payment(String orderNo) {
        OrderClient.OrderDto order = orderClient.getOrder(orderNo);
        try{
            PaymentInfo.PaymentDto payment = paymentService.payment(orderNo, order.paymentAmt());
            return PaymentResult.Payment.of(payment);
        } catch(Exception e) {
            paymentService.failed(order.orderNo(), order.paymentAmt(), e.toString());
            throw e;
        }
    }
}
