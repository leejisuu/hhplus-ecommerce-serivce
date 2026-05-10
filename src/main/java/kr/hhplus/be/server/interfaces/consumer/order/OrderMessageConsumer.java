package kr.hhplus.be.server.interfaces.consumer.order;

import kr.hhplus.be.server.application.order.OrderSagaOrchestrator;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderMessageConsumer {

    private final OrderSagaOrchestrator orderSagaOrchestrator;

    @KafkaListener(topics = "${payment-api.paid.topic-name}", groupId = "order-service.confirm-on-pay")
    public void consume(PaymentEvent.Paid event) {
        orderSagaOrchestrator.confirm(event.orderNo());
    }

    @KafkaListener(topics = "${payment-api.failed.topic-name}", groupId = "order-service.fail-on-pay")
    public void consume(PaymentEvent.Failed event) {
        orderSagaOrchestrator.fail(event.orderNo());
    }
}