package kr.hhplus.be.server.interfaces.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.order.event.OrderEvent;
import kr.hhplus.be.server.domain.order.outbox.OrderOutbox;
import kr.hhplus.be.server.domain.order.outbox.OrderOutboxService;
import kr.hhplus.be.server.domain.order.producer.OrderMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderOutboxScheduler {

    private final OrderMessageProducer orderMessageProducer;
    private final OrderOutboxService orderOutboxService;

    @Scheduled(cron = "*/1 * * * * *")
    public void resendOrderConfirmedMessage() {
        List<OrderOutbox> unsentOrderMessage = orderOutboxService.findUnsentOrderConfirmedMessage();
        ObjectMapper objectMapper = new ObjectMapper();
        for(OrderOutbox orderOutbox : unsentOrderMessage) {
            try {
                OrderEvent.Confirmed event = objectMapper.readValue(orderOutbox.getPayload(), OrderEvent.Confirmed.class);
                orderMessageProducer.sendOrderConfirmedInfo(event);
                orderOutboxService.markAsComplete(event.eventId());
            } catch (JsonProcessingException e) {
                throw new RuntimeException("OrderOutbox payload parsing error ", e);
            }
        }
    }
}
