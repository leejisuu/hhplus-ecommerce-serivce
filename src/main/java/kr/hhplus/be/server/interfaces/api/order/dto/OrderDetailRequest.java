package kr.hhplus.be.server.interfaces.api.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderDetailRequest {
    private Long productId;
    private int qantity;

    @Builder
    public OrderDetailRequest(Long productId, int qantity) {
        this.productId = productId;
        this.qantity = qantity;
    }
}
