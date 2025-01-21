package kr.hhplus.be.server.infrastructure.product.dto;

import lombok.Builder;

import java.math.BigDecimal;

public record TopSellingProductDto(
        Long productId,
        String name,
        int quantity,
        BigDecimal price,
        Long totalSales
) {
    @Builder
    public TopSellingProductDto {}
}
