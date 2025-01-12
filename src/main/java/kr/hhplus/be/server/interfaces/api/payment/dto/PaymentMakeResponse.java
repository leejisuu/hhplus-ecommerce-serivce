package kr.hhplus.be.server.interfaces.api.payment.dto;

import kr.hhplus.be.server.domain.payment.Payment;

import java.time.LocalDateTime;

public record PaymentMakeResponse(
        Long id,
        Long orderId,
        String status,
        int totalAmt,
        LocalDateTime createdAt
) {
    public static PaymentMakeResponse from(Payment payment) {
        return new PaymentMakeResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getStatus().name(),
                payment.getTotalAmt(),
                payment.getCreatedAt()
        );
    }
}
