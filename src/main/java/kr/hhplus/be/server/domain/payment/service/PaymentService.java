package kr.hhplus.be.server.domain.payment.service;

import kr.hhplus.be.server.domain.payment.dto.info.PaymentInfo;
import kr.hhplus.be.server.domain.payment.entity.Payment;
import kr.hhplus.be.server.domain.payment.event.PaymentEvent;
import kr.hhplus.be.server.domain.payment.event.PaymentEventPublisher;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher paymentEventPublisher;


    @Transactional
    public PaymentInfo.PaymentDto payment(String orderNo, BigDecimal paymentAmt) {
        Payment payment = Payment.create(orderNo, paymentAmt);
        paymentEventPublisher.publish(PaymentEvent.Paid.create(orderNo, paymentAmt));
        return PaymentInfo.PaymentDto.of(paymentRepository.save(payment));
    }

    @Transactional
    public void failed(String orderNo, BigDecimal paymentAmt, String failReason) {
        paymentEventPublisher.publish(PaymentEvent.Failed.create(orderNo, paymentAmt, failReason));
    }


}

