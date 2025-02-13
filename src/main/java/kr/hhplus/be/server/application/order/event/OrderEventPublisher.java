package kr.hhplus.be.server.application.order.event;

import kr.hhplus.be.server.domain.order.event.OrderCreateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class OrderEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publish(OrderCreateEvent orderCreateEvent) {
        applicationEventPublisher.publishEvent(orderCreateEvent);
    }
}
