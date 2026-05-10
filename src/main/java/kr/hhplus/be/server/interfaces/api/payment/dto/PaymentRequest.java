package kr.hhplus.be.server.interfaces.api.payment.dto;

public class PaymentRequest {
    public record Payment(
            String ordrNo
    ) {
    }
}
