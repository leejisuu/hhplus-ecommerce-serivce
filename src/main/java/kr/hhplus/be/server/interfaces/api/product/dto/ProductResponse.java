package kr.hhplus.be.server.interfaces.api.product.dto;

import kr.hhplus.be.server.domain.order.entity.OrderDetail;
import kr.hhplus.be.server.domain.product.dto.ProductInfo;
import kr.hhplus.be.server.domain.product.entity.Product;

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
        public static ProductResponse.TopSelling of(ProductInfo.TopSelling topSelling) {
            return new ProductResponse.TopSelling (
                    topSelling.productId(),
                    topSelling.name(),
                    topSelling.price(),
                    topSelling.totalQuantity()
            );
        }

    }
}