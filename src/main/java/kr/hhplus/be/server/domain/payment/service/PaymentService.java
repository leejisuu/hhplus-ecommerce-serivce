package kr.hhplus.be.server.domain.payment.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.payment.dto.info.PaymentInfo;
import kr.hhplus.be.server.domain.payment.entity.Payment;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentInfo.PaymentDto payment(Long orderId, BigDecimal totalOriginalAmt, BigDecimal discountAmt, Long issuedCouponId) {
        Payment payment = Payment.create(orderId, totalOriginalAmt, discountAmt, issuedCouponId);
        return PaymentInfo.PaymentDto.of(paymentRepository.save(payment));
    }
}
