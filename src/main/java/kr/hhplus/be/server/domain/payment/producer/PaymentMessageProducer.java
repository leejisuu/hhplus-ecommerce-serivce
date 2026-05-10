package kr.hhplus.be.server.domain.payment.producer;

import kr.hhplus.be.server.domain.payment.event.PaymentEvent;

public interface PaymentMessageProducer {
    void sendPaymentPaidInfo(PaymentEvent.Paid event);

    void sendPaymentFailedInfo(PaymentEvent.Failed event);
}
