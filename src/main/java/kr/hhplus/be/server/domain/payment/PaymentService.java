package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.interfaces.api.payment.dto.PaymentMakeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentMakeResponse makePayment(Order order) {
        Payment payment = Payment.create(order);
        return PaymentMakeResponse.from(paymentRepository.makePayment(payment));
    }
}
