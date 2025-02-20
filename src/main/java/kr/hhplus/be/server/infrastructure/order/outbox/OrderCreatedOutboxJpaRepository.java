package kr.hhplus.be.server.infrastructure.order.outbox;

import kr.hhplus.be.server.domain.common.outbox.OutboxStatus;
import kr.hhplus.be.server.domain.order.outbox.OrderCreatedOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderCreatedOutboxJpaRepository extends JpaRepository<OrderCreatedOutbox, String> {
    List<OrderCreatedOutbox> findAllByStatus(OutboxStatus outboxStatus);
}
