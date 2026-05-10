package kr.hhplus.be.server.domain.order.outbox;

import kr.hhplus.be.server.domain.common.outbox.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderOutboxService {

    private final OrderOutboxRepository orderOutboxRepository;

    @Transactional
    public void save(OrderOutbox orderOutbox) {
        orderOutboxRepository.save(orderOutbox);
    }

    @Transactional
    public void markAsComplete(String eventId) {
        Optional<OrderOutbox> orderOutbox = orderOutboxRepository.getOutbox(eventId);
        orderOutbox.ifPresentOrElse(
                OrderOutbox::complete,
                () -> log.warn("Outbox not found for complete. eventId={}", eventId)
        );
    }

    @Transactional(readOnly = true)
    public List<OrderOutbox> findUnsentOrderConfirmedMessage() {
        return orderOutboxRepository.findAllByStatusAndEventType(OutboxStatus.INIT, OrderOutboxEventType.CONFIRMED);
    }
}
