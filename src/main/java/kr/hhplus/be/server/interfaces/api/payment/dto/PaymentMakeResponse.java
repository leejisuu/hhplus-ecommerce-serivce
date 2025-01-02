package kr.hhplus.be.server.interfaces.api.payment.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PaymentMakeResponse {

    private Long id;
    private Long orderId;
    private String status;
    private int totalAmt;
    private LocalDateTime createdAt;

    @Builder
    public PaymentMakeResponse(Long id, Long orderId, String status, int totalAmt, LocalDateTime createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.status = status;
        this.totalAmt = totalAmt;
        this.createdAt = createdAt;
    }
}
