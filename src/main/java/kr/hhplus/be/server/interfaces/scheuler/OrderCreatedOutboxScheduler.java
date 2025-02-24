package kr.hhplus.be.server.interfaces.scheuler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.order.event.OrderEvent;
import kr.hhplus.be.server.domain.order.outbox.OrderCreatedOutbox;
import kr.hhplus.be.server.domain.order.outbox.OrderCreatedOutboxService;
import kr.hhplus.be.server.domain.order.producer.OrderMessageProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderCreatedOutboxScheduler {

    private final OrderMessageProducer orderMessageProducer;
    private final OrderCreatedOutboxService orderCreatedOutboxService;

    @Scheduled(cron = "0 */5 * * * *")
    public void resendOrderCreatedMessage() {
        List<OrderCreatedOutbox> unsentOrderMessage = orderCreatedOutboxService.findUnsentOrderMessage();
        ObjectMapper objectMapper = new ObjectMapper();
        for(OrderCreatedOutbox orderCreatedOutbox : unsentOrderMessage) {
            try {
                OrderEvent.Created event = objectMapper.readValue(orderCreatedOutbox.getPayload(), OrderEvent.Created.class);
                orderMessageProducer.sendOrderCreatedInfo(event);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("orderCreatedOutbox payload parsing error ", e);
            }
        }
    }
}
