package kr.hhplus.be.server.interfaces.api.order.dto;

import kr.hhplus.be.server.interfaces.api.product.dto.ProductResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderCreateResponse {

    private Long id;
    private String status;
    private int netAmt;
    private int discountAmt;
    private int totalAmt;
    private Long couponId;
    private List<ProductResponse> products;
    private LocalDateTime createdAt;

    @Builder

    public OrderCreateResponse(Long id, String status, int netAmt, int discountAmt, int totalAmt, Long couponId, List<ProductResponse> products, LocalDateTime createdAt) {
        this.id = id;
        this.status = status;
        this.netAmt = netAmt;
        this.discountAmt = discountAmt;
        this.totalAmt = totalAmt;
        this.couponId = couponId;
        this.products = products;
        this.createdAt = createdAt;
    }
}
