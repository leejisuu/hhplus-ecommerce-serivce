package kr.hhplus.be.server.interfaces.api.order.dto;

import kr.hhplus.be.server.domain.order.dto.info.TopSellingProductInfo;

import java.math.BigDecimal;

public record TopSellingProductResponse(
        Long productId,
        String name,
        int quantity,
        BigDecimal price,
        Long totalSales
) {
    public static TopSellingProductResponse from(TopSellingProductInfo topSellingProductInfo) {
        return new TopSellingProductResponse(
                topSellingProductInfo.productId(),
                topSellingProductInfo.name(),
                topSellingProductInfo.quantity(),
                topSellingProductInfo.price(),
                topSellingProductInfo.totalSales()
        );
    }
}
