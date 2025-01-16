package kr.hhplus.be.server.domain.payment.service;

import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.payment.dto.info.PaymentInfo;
import kr.hhplus.be.server.domain.payment.entity.Payment;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import kr.hhplus.be.server.interfaces.api.payment.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaymentInfo.PaymentDto payment(Long orderId, BigDecimal totalOriginalAmt, BigDecimal discountAmt, Long issuedCouponId) {
        Payment payment = Payment.create(orderId, totalOriginalAmt, discountAmt, issuedCouponId);
        return PaymentInfo.PaymentDto.of(paymentRepository.save(payment));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void failPayment(Long paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId);

        payment.failPayment();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void completePayment(Long paymentId) {
        Payment payment = paymentRepository.findByPaymentId(paymentId);

        payment.completePayment();
    }
}
