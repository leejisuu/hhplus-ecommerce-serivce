package kr.hhplus.be.server.interfaces.api.product.dto;

import kr.hhplus.be.server.domain.order.entity.OrderDetail;
import kr.hhplus.be.server.domain.product.dto.ProductInfo;
import kr.hhplus.be.server.domain.product.entity.Product;

import java.math.BigDecimal;

public record ProductResponse(
        String name,
        BigDecimal price,
        int quantity
) {
    public static ProductResponse of(ProductInfo productInfo) {
        return new ProductResponse(
                productInfo.name(),
                productInfo.price(),
                productInfo.quantity()
        );
    }
}
