package kr.hhplus.be.server.infrastructure.payment.outbox;

import kr.hhplus.be.server.domain.common.outbox.OutboxStatus;
import kr.hhplus.be.server.domain.payment.outbox.PaymentOutbox;
import kr.hhplus.be.server.domain.payment.outbox.PaymentOutboxEventType;
import kr.hhplus.be.server.domain.payment.outbox.PaymentOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class PaymentOutboxRepositoryImpl implements PaymentOutboxRepository {
    private final PaymentOutboxJpaRepository outboxJpaRepository;

    @Override
    public void save(PaymentOutbox outbox) {
        outboxJpaRepository.save(outbox);
    }

    @Override
    public List<PaymentOutbox> findAllByStatusAndEventType(OutboxStatus status, PaymentOutboxEventType eventType) {
        return outboxJpaRepository.findAllByStatusAndEventType(status, eventType);
    }

    @Override
    public Optional<PaymentOutbox> getOutbox(String eventId) {
        return outboxJpaRepository.findByEventId(eventId);
    }
}
