package kr.hhplus.be.server.domain.payment.outbox;

import kr.hhplus.be.server.domain.common.outbox.OutboxStatus;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentOutboxService {
    private final PaymentOutboxRepository paymentOutboxRepository;

    @Transactional
    public void save(PaymentOutbox outbox) {
        paymentOutboxRepository.save(outbox);
    }

    @Transactional
    public void markAsComplete(String eventId) {
        PaymentOutbox outbox = paymentOutboxRepository.getOutbox(eventId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_OUTBOX_NOT_FOUND));
        outbox.complete();
    }

    @Transactional(readOnly = true)
    public List<PaymentOutbox> findUnsentPaymentPaidMessage() {
        return paymentOutboxRepository.findAllByStatusAndEventType(OutboxStatus.INIT, PaymentOutboxEventType.COMPLETED);
    }

    @Transactional(readOnly = true)
    public List<PaymentOutbox> findUnsentPaymentFailedMessage() {
        return paymentOutboxRepository.findAllByStatusAndEventType(OutboxStatus.INIT, PaymentOutboxEventType.FAILED);
    }
}
