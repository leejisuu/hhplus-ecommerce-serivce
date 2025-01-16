package kr.hhplus.be.server.interfaces.api.product.dto;

import kr.hhplus.be.server.domain.order.entity.OrderDetail;
import kr.hhplus.be.server.domain.product.dto.ProductInfo;
import kr.hhplus.be.server.domain.product.entity.Product;

import java.math.BigDecimal;

public class ProductResponse {

    public record Stock(
            Long id,
            String name,
            BigDecimal price,
            int quantity
    ) {
        public static ProductResponse.Stock of(ProductInfo.Stock productInfo) {
            return new ProductResponse.Stock (
                    productInfo.id(),
                    productInfo.name(),
                    productInfo.price(),
                    productInfo.quantity()
            );
        }

    }

    public record TopSelling(
            Long productId,
            String name,
            int quantity,
            BigDecimal price,
            Long totalSales
    ) {
        public static ProductResponse.TopSelling of(ProductInfo.TopSelling topSelling) {
            return new ProductResponse.TopSelling (
                    topSelling.productId(),
                    topSelling.name(),
                    topSelling.quantity(),
                    topSelling.price(),
                    topSelling.totalSales()
            );
        }

    }
}