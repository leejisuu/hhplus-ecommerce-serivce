package kr.hhplus.be.server.infrastructure.order.outbox;

import kr.hhplus.be.server.domain.common.outbox.OutboxStatus;
import kr.hhplus.be.server.domain.order.outbox.OrderOutbox;
import kr.hhplus.be.server.domain.order.outbox.OrderOutboxEventType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderOutboxJpaRepository extends JpaRepository<OrderOutbox, Long> {
    List<OrderOutbox> findAllByStatusAndEventType(OutboxStatus status, OrderOutboxEventType eventType);

    Optional<OrderOutbox> findByEventId(String eventId);
}
