package kr.hhplus.be.server.infrastructure.order.producer;

import kr.hhplus.be.server.domain.order.event.OrderEvent;
import kr.hhplus.be.server.domain.order.producer.OrderMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderMessageProducerImpl implements OrderMessageProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${order-api.order-created.topic-name}")
    private String topicName;

    @Override
    public void sendOrderCreatedInfo(OrderEvent.Created event) {
        kafkaTemplate.send(topicName, event.orderId().toString(), event);
    }
}
