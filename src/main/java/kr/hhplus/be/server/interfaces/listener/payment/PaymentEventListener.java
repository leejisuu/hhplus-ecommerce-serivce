package kr.hhplus.be.server.interfaces.listener.payment;

import kr.hhplus.be.server.domain.payment.event.PaymentEvent;
import kr.hhplus.be.server.domain.payment.outbox.PaymentOutbox;
import kr.hhplus.be.server.domain.payment.outbox.PaymentOutboxService;
import kr.hhplus.be.server.domain.payment.producer.PaymentMessageProducer;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentEventListener {
    private final PaymentOutboxService paymentOutboxService;
    private final PaymentMessageProducer paymentMessageProducer;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void saveOutbox(PaymentEvent.Paid event) {
        paymentOutboxService.save(PaymentOutbox.of(event));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPaymentPaidInfo(PaymentEvent.Paid event) {
        try {
            paymentMessageProducer.sendPaymentPaidInfo(event);
        } catch (Exception e) {
            log.error("결제 성공 메세지 발행 실패 eventId={}", event.eventId(), e);
            return;
        }
        try {
            paymentOutboxService.markAsComplete(event.eventId());
        } catch (CustomException e) {
            log.error("결제 outbox markAsComplete 실패 - 정합성 점검 필요. eventId={}", event.eventId(), e);
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void saveOutbox(PaymentEvent.Failed event) {
        paymentOutboxService.save(PaymentOutbox.of(event));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendPaymentFailedInfo(PaymentEvent.Failed event) {
        try {
            paymentMessageProducer.sendPaymentFailedInfo(event);
        } catch (Exception e) {
            log.error("결제 성공 메세지 발행 실패 eventId={}", event.eventId(), e);
            return;
        }
        try {
            paymentOutboxService.markAsComplete(event.eventId());
        } catch (CustomException e) {
            log.error("결제 outbox markAsComplete 실패 - 정합성 점검 필요. eventId={}", event.eventId(), e);
        }
    }
}
