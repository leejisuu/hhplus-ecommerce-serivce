package kr.hhplus.be.server.domain.product.dto;

import kr.hhplus.be.server.domain.product.entity.Product;

import java.math.BigDecimal;

public class ProductInfo {

    public record Stock(
            Long id,
            String name,
            BigDecimal price,
            int quantity
    ) {
        public static ProductInfo.Stock of(StockDto stockDto) {
            return new ProductInfo.Stock (
                    stockDto.id(),
                    stockDto.name(),
                    stockDto.price(),
                    stockDto.quantity()
            );
        }
    }

    public record TopSelling(
            Long productId,
            String name,
            BigDecimal price,
            int totalQuantity
    ) {
        public static ProductInfo.TopSelling of(TopSellingProductDto topSellingProductDto) {
            return new ProductInfo.TopSelling(
                    topSellingProductDto.getProductId(),
                    topSellingProductDto.getName(),
                    topSellingProductDto.getPrice(),
                    topSellingProductDto.getTotalQuantity()
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