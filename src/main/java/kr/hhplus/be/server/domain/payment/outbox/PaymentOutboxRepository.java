package kr.hhplus.be.server.domain.payment.outbox;

import kr.hhplus.be.server.domain.common.outbox.OutboxStatus;

import java.util.List;
import java.util.Optional;

public interface PaymentOutboxRepository {
    void save(PaymentOutbox outbox);

    List<PaymentOutbox> findAllByStatusAndEventType(OutboxStatus outboxStatus, PaymentOutboxEventType paymentOutboxEventType);

    Optional<PaymentOutbox> getOutbox(String eventId);
}
