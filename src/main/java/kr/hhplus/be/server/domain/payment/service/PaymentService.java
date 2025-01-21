package kr.hhplus.be.server.domain.payment.service;

import kr.hhplus.be.server.domain.payment.dto.info.PaymentInfo;
import kr.hhplus.be.server.domain.payment.entity.Payment;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
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
    public PaymentInfo.PaymentDto fail(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId);

        payment.fail();

        return PaymentInfo.PaymentDto.of(payment);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PaymentInfo.PaymentDto complete(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId);

        payment.complete();

        return PaymentInfo.PaymentDto.of(payment);
    }
}
