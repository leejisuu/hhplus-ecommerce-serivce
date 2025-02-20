package kr.hhplus.be.server.domain.order.outbox;

import kr.hhplus.be.server.domain.common.outbox.OutboxStatus;

import java.util.List;
import java.util.Optional;

public interface OrderCreatedOutboxRepository {
    void save(OrderCreatedOutbox orderCreatedOutbox);

    Optional<OrderCreatedOutbox> getOutbox(String messageId);

    List<OrderCreatedOutbox> findByAllByStatus(OutboxStatus outboxStatus);
}
