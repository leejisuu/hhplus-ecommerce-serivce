package kr.hhplus.be.server.interfaces.consumer.order;

import kr.hhplus.be.server.domain.order.event.OrderEvent;
import kr.hhplus.be.server.domain.order.outbox.OrderCreatedOutbox;
import kr.hhplus.be.server.domain.order.outbox.OrderCreatedOutboxService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class OrderMessageConsumer {

    private final OrderCreatedOutboxService orderCreatedOutboxService;
    private static final String GROUP_ID = "order-created";

    @KafkaListener(topics = "${order-api.order-created.topic-name}", groupId = GROUP_ID)
    @Transactional
    public void consume(OrderEvent.Created event) {
        OrderCreatedOutbox orderCreatedOutbox = orderCreatedOutboxService.getOutbox(event.messageId())
                                                    .orElseThrow(() -> new RuntimeException("OrderCreatedOutbox not found"));
        orderCreatedOutbox.complete();
    }
}
