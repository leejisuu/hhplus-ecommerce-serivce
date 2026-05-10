package kr.hhplus.be.server.domain.order.outbox;

import kr.hhplus.be.server.domain.common.outbox.OutboxStatus;

import java.util.List;
import java.util.Optional;

public interface OrderOutboxRepository {
    void save(OrderOutbox orderOutbox);

    Optional<OrderOutbox> getOutbox(String eventId);

    List<OrderOutbox> findAllByStatusAndEventType(OutboxStatus status, OrderOutboxEventType eventType);
}
