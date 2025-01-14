package kr.hhplus.be.server.domain.product.dto;

import kr.hhplus.be.server.infrastructure.product.dto.ProductDTO;

import java.math.BigDecimal;

public record ProductInfo(
        String name,
        BigDecimal price,
        int quantity
) {

    public static ProductInfo of(ProductDTO productDTO) {
        return new ProductInfo(
                productDTO.name(),
                productDTO.price(),
                productDTO.quantity()
        );
    }
}
