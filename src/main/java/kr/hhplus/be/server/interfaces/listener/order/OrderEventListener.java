package kr.hhplus.be.server.interfaces.listener.order;

import kr.hhplus.be.server.domain.order.event.OrderEvent;
import kr.hhplus.be.server.domain.order.producer.OrderMessageProducer;
import kr.hhplus.be.server.domain.order.outbox.OrderCreatedOutbox;
import kr.hhplus.be.server.domain.order.outbox.OrderCreatedOutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
public class OrderEventListener {

    private final OrderCreatedOutboxService orderOutboxService;
    private final OrderMessageProducer orderMessageProducer;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void saveCreatedOutbox(OrderEvent.Created event) {
        orderOutboxService.save(OrderCreatedOutbox.of(event));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendOrderCreatedInfo(OrderEvent.Created event) {
        orderMessageProducer.sendOrderCreatedInfo(event);
    }

}
