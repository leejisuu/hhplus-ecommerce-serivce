package kr.hhplus.be.server.domain.order.outbox;

import kr.hhplus.be.server.domain.common.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderCreatedOutboxService {

    private final OrderCreatedOutboxRepository orderOutboxRepository;

    public void save(OrderCreatedOutbox orderCreatedOutbox) {
        orderOutboxRepository.save(orderCreatedOutbox);
    }

    public Optional<OrderCreatedOutbox> getOutbox(String messageId) {
        return orderOutboxRepository.getOutbox(messageId);
    }

    public List<OrderCreatedOutbox> findUnsentOrderMessage() {
        return orderOutboxRepository.findByAllByStatus(OutboxStatus.INIT);
    }
}
