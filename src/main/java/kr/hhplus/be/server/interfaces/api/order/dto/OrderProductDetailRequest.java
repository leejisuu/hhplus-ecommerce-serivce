package kr.hhplus.be.server.interfaces.api.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderProductDetailRequest {
    private Long productId;
    private int qantity;

    @Builder
    public OrderProductDetailRequest(Long productId, int qantity) {
        this.productId = productId;
        this.qantity = qantity;
    }
}
