package kr.hhplus.be.server.interfaces.api.product.dto;

import kr.hhplus.be.server.domain.product.dto.ProductInfo;
import kr.hhplus.be.server.domain.product.dto.TopSellingProductInfo;

import java.math.BigDecimal;

public class ProductResponse {

    public record Stock(
            Long productId,
            String name,
            BigDecimal price,
            int quantity
    ) {
        public static ProductResponse.Stock of(ProductInfo.Stock productInfo) {
            return new ProductResponse.Stock (
                    productInfo.productId(),
                    productInfo.name(),
                    productInfo.price(),
                    productInfo.quantity()
            );
        }

    }

    public record TopSelling(
            Long productId,
            String name,
            BigDecimal price,
            int totalQuantity
    ) {
        public static ProductResponse.TopSelling of(TopSellingProductInfo topSelling) {
            return new ProductResponse.TopSelling (
                    topSelling.getProductId(),
                    topSelling.getName(),
                    topSelling.getPrice(),
                    topSelling.getTotalQuantity()
            );
        }
    }
}