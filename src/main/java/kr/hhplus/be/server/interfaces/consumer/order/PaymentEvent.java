package kr.hhplus.be.server.interfaces.consumer.order;

import java.math.BigDecimal;

public class PaymentEvent {

    public record Paid(
            String eventId,
            String orderNo,
            BigDecimal paymentAmt
    ) {
    }

    public record Failed(
            String eventId,
            String orderNo,
            BigDecimal paymentAmt,
            String failedReason
    ) {
    }
}
