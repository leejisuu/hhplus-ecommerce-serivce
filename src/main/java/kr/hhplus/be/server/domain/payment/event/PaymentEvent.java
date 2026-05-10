package kr.hhplus.be.server.domain.payment.event;

import java.math.BigDecimal;
import java.util.UUID;

public class PaymentEvent {

    public record Paid(
            String eventId,
            String orderNo,
            BigDecimal paymentAmt
    ) {
        public static PaymentEvent.Paid create(String orderNo, BigDecimal paymentAmt) {
            return new PaymentEvent.Paid(UUID.randomUUID().toString(), orderNo, paymentAmt);
        }
    }

    public record Failed(
            String eventId,
            String orderNo,
            BigDecimal paymentAmt,
            String failedReason
    ) {
        public static PaymentEvent.Failed create(String orderNo, BigDecimal paymentAmt, String failedReason) {
            return new PaymentEvent.Failed(UUID.randomUUID().toString(), orderNo, paymentAmt, failedReason);
        }
    }
}
