package kr.hhplus.be.server.interfaces.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.payment.event.PaymentEvent;
import kr.hhplus.be.server.domain.payment.outbox.PaymentOutbox;
import kr.hhplus.be.server.domain.payment.outbox.PaymentOutboxService;
import kr.hhplus.be.server.domain.payment.producer.PaymentMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentOutboxScheduler {

    private final PaymentOutboxService paymentOutboxService;
    private final PaymentMessageProducer paymentMessageProducer;

    @Scheduled(cron = "*/1 * * * * *")
    public void resendPaymentPaidMessage() {
        List<PaymentOutbox> unsentPaymentMessage = paymentOutboxService.findUnsentPaymentPaidMessage();
        ObjectMapper objectMapper = new ObjectMapper();
        for(PaymentOutbox paymentOutbox : unsentPaymentMessage) {
            try {
                PaymentEvent.Paid event = objectMapper.readValue(paymentOutbox.getPayload(), PaymentEvent.Paid.class);
                paymentMessageProducer.sendPaymentPaidInfo(event);
                paymentOutboxService.markAsComplete(event.eventId());
            } catch (JsonProcessingException e) {
                throw new RuntimeException("PaymentOutbox payload parsing error ", e);
            }
        }
    }

    @Scheduled(cron = "*/1 * * * * *")
    public void resendPaymentFailedMessage() {
        List<PaymentOutbox> unsentOrderMessage = paymentOutboxService.findUnsentPaymentFailedMessage();
        ObjectMapper objectMapper = new ObjectMapper();
        for(PaymentOutbox paymentOutbox : unsentOrderMessage) {
            try {
                PaymentEvent.Failed event = objectMapper.readValue(paymentOutbox.getPayload(), PaymentEvent.Failed.class);
                paymentMessageProducer.sendPaymentFailedInfo(event);
                paymentOutboxService.markAsComplete(event.eventId());
            } catch (JsonProcessingException e) {
                throw new RuntimeException("PaymentOutbox payload parsing error ", e);
            }
        }
    }
}
