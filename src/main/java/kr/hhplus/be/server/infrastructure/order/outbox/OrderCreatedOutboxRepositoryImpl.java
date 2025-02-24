package kr.hhplus.be.server.infrastructure.order.outbox;

import kr.hhplus.be.server.domain.common.outbox.OutboxStatus;
import kr.hhplus.be.server.domain.order.outbox.OrderCreatedOutbox;
import kr.hhplus.be.server.domain.order.outbox.OrderCreatedOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class OrderCreatedOutboxRepositoryImpl implements OrderCreatedOutboxRepository {

    private final OrderCreatedOutboxJpaRepository orderOutboxJpaRepository;

    @Override
    public void save(OrderCreatedOutbox orderCreatedOutbox) {
        orderOutboxJpaRepository.save(orderCreatedOutbox);
    }

    @Override
    public Optional<OrderCreatedOutbox> getOutbox(String messageId) {
        return orderOutboxJpaRepository.findById(messageId);
    }

    @Override
    public List<OrderCreatedOutbox> findByAllByStatus(OutboxStatus outboxStatus) {
        return orderOutboxJpaRepository.findAllByStatus(outboxStatus);
    }
}
