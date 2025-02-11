package kr.hhplus.be.server.domain.product.dto;

import java.math.BigDecimal;

public record StockDto(
        Long productId,
        String name,
        BigDecimal price,
        int quantity
) {
}
