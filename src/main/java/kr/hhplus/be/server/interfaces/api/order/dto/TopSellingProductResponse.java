package kr.hhplus.be.server.interfaces.api.order.dto;

import kr.hhplus.be.server.domain.order.dto.TopSellingProductInfo;

public record TopSellingProductResponse(
        Long productId,
        String name,
        int quantity,
        int price,
        Long totalSales
) {
    public static TopSellingProductResponse of(TopSellingProductInfo topSellingProductInfo) {
        return new TopSellingProductResponse(
                topSellingProductInfo.productId(),
                topSellingProductInfo.name(),
                topSellingProductInfo.quantity(),
                topSellingProductInfo.price(),
                topSellingProductInfo.totalSales()
        );
    }
}
