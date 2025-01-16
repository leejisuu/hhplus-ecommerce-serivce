package kr.hhplus.be.server.interfaces.api.payment.dto;

public class PaymentRequest {
    public record Payment(
            Long userId,
            Long orderId,
            Long issuedCouponId
    ) {
    }
}
