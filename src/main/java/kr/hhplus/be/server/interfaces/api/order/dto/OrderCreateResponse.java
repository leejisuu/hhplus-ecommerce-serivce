package kr.hhplus.be.server.interfaces.api.order.dto;

import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.interfaces.api.product.dto.ProductResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record OrderCreateResponse (
        Long id,
        String status,
        int netAmt,
        int discountAmt,
        int totalAmt,
        Long couponId,
        List<ProductResponse> products
) {
    public static OrderCreateResponse of(Order order) {

        List<ProductResponse> products = order.getOrderDetails().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());

        return new OrderCreateResponse(
                order.getId(),
                order.getStatus().name(),
                order.getNetAmt(),
                order.getDiscountAmt(),
                order.getTotalAmt(),
                order.getIssuedCoupon().getId(),
                products
        );
    }
}

