package kr.hhplus.be.server.domain.product.dto;

import kr.hhplus.be.server.domain.product.entity.Product;

import java.math.BigDecimal;

public class ProductInfo {

    public record Stock(
            Long productId,
            String name,
            BigDecimal price,
            int quantity
    ) {
        public static ProductInfo.Stock of(StockDto stockDto) {
            return new ProductInfo.Stock (
                    stockDto.productId(),
                    stockDto.name(),
                    stockDto.price(),
                    stockDto.quantity()
            );
        }
    }

    public record ProductDto(
            Long id,
            String name,
            BigDecimal price
    ) {
        public static ProductInfo.ProductDto of(Product product) {
            return new ProductInfo.ProductDto(
                    product.getId(),
                    product.getName(),
                    product.getPrice()
            );
        }
    }
}