package kr.hhplus.be.server.infrastructure.payment.producer;

import kr.hhplus.be.server.domain.payment.event.PaymentEvent;
import kr.hhplus.be.server.domain.payment.producer.PaymentMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Component
public class PaymentMessageProducerImpl implements PaymentMessageProducer {

    @Value("${payment-api.paid.topic-name}")
    private String paidTopic;

    @Value("${payment-api.failed.topic-name}")
    private String failedTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendPaymentPaidInfo(PaymentEvent.Paid event) {
        kafkaTemplate.send(paidTopic, event.orderNo(), event);
    }

    @Override
    public void sendPaymentFailedInfo(PaymentEvent.Failed event) {
        kafkaTemplate.send(failedTopic, event.orderNo(), event);
    }
}
