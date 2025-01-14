package kr.hhplus.be.server.infrastructure.product.dto;

import java.math.BigDecimal;

public record ProductDTO(
        String name,
        BigDecimal price,
        int quantity
) {
}
