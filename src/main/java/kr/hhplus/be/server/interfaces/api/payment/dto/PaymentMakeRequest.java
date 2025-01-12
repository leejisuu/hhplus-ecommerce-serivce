package kr.hhplus.be.server.interfaces.api.payment.dto;

public record PaymentMakeRequest (
        Long userId,
        Long orderId
) {
}

