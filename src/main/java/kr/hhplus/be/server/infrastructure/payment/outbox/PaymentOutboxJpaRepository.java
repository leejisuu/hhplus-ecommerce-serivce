package kr.hhplus.be.server.infrastructure.payment.outbox;

import kr.hhplus.be.server.domain.common.outbox.OutboxStatus;
import kr.hhplus.be.server.domain.payment.outbox.PaymentOutbox;
import kr.hhplus.be.server.domain.payment.outbox.PaymentOutboxEventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentOutboxJpaRepository extends JpaRepository<PaymentOutbox, Long> {
    List<PaymentOutbox> findAllByStatusAndEventType(OutboxStatus status, PaymentOutboxEventType eventType);

    Optional<PaymentOutbox> findByEventId(String eventId);
}
