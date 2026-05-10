package kr.hhplus.be.server.infrastructure.order.outbox;

import kr.hhplus.be.server.domain.common.outbox.OutboxStatus;
import kr.hhplus.be.server.domain.order.outbox.OrderOutbox;
import kr.hhplus.be.server.domain.order.outbox.OrderOutboxEventType;
import kr.hhplus.be.server.domain.order.outbox.OrderOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class OrderOutboxRepositoryImpl implements OrderOutboxRepository {

    private final OrderOutboxJpaRepository orderOutboxJpaRepository;

    @Override
    public void save(OrderOutbox orderOutbox) {
        orderOutboxJpaRepository.save(orderOutbox);
    }

    @Override
    public Optional<OrderOutbox> getOutbox(String eventId) {
        return orderOutboxJpaRepository.findByEventId(eventId);
    }

    @Override
    public List<OrderOutbox> findAllByStatusAndEventType(OutboxStatus status, OrderOutboxEventType eventType) {
        return orderOutboxJpaRepository.findAllByStatusAndEventType(status, eventType);
    }
}
