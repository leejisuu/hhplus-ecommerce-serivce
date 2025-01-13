package kr.hhplus.be.server.interfaces.api.product.dto;

import kr.hhplus.be.server.domain.order.entity.OrderDetail;
import kr.hhplus.be.server.domain.product.entity.Product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        int quantity,
        BigDecimal price
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getProductStock().getQuantity(),
                product.getPrice()
        );
    }

    public static ProductResponse from(OrderDetail orderDetail) {
        return new ProductResponse(
                orderDetail.getId(),
                orderDetail.getProduct().getName(),
                orderDetail.getQuantity(),
                orderDetail.getPrice()
        );
    }
}
