package kr.hhplus.be.server.interfaces.listener.order;

import kr.hhplus.be.server.domain.order.event.OrderEvent;
import kr.hhplus.be.server.domain.order.outbox.OrderOutbox;
import kr.hhplus.be.server.domain.order.producer.OrderMessageProducer;
import kr.hhplus.be.server.domain.order.outbox.OrderOutboxService;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderEventListener {

    private final OrderOutboxService orderOutboxService;
    private final OrderMessageProducer orderMessageProducer;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void saveOutbox(OrderEvent.Confirmed event) {
        orderOutboxService.save(OrderOutbox.of(event));
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendOrderConfirmedInfo(OrderEvent.Confirmed event) {
        try {
            orderMessageProducer.sendOrderConfirmedInfo(event);
        } catch (Exception e) {
            log.error("주문 확정 메세지 발행 실패 eventId={}", event.eventId(), e);
            return;
        }
        try {
            orderOutboxService.markAsComplete(event.eventId());
        } catch (CustomException e) {
            log.error("주문 outbox markAsComplete 실패 - 정합성 점검 필요. eventId={}", event.eventId(), e);
        }
    }

}
