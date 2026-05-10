package kr.hhplus.be.server.domain.payment.event;

public interface PaymentEventPublisher {
    void publish(PaymentEvent.Paid paidEvent);

    void publish(PaymentEvent.Failed failedEvent);
}
