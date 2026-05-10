package kr.hhplus.be.server.infrastructure.payment.event;

import kr.hhplus.be.server.domain.payment.event.PaymentEvent;
import kr.hhplus.be.server.domain.payment.event.PaymentEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisherImpl implements PaymentEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publish(PaymentEvent.Paid paidEvent) {
        eventPublisher.publishEvent(paidEvent);
    }

    @Override
    public void publish(PaymentEvent.Failed failedEvent) {
        eventPublisher.publishEvent(failedEvent);
    }
}
