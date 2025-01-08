package kr.hhplus.be.server.interfaces.api.payment.dto;

import java.time.LocalDateTime;

public record PaymentMakeResponse(
        Long id,
        Long orderId,
        String status,
        int totalAmt,
        LocalDateTime createdAt
) {
}
