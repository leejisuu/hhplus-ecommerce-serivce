package kr.hhplus.be.server.interfaces.api.product.dto;

import kr.hhplus.be.server.domain.product.entity.Product;

public record ProductResponse(
        Long id,
        String name,
        int stock,
        int price
) {
    public static ProductResponse of(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getProductStock().getQuantity(),
                product.getPrice()
        );
    }
}
