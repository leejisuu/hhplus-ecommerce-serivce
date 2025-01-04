package kr.hhplus.be.server.interfaces.api.payment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PaymentMakeRequest {
    private Long userId;
    private Long orderId;

    @Builder
    public PaymentMakeRequest(Long userId, Long orderId) {
        this.userId = userId;
        this.orderId = orderId;
    }
}
