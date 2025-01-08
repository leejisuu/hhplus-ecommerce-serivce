package kr.hhplus.be.server.interfaces.api.order.dto;

import kr.hhplus.be.server.interfaces.api.product.dto.ProductResponse;

import java.time.LocalDateTime;
import java.util.List;

public record OrderCreateResponse (
        Long id,
        String status,
        int netAmt,
        int discountAmt,
        int totalAmt,
        Long couponId,
        List<ProductResponse> products,
        LocalDateTime createdAt
) {
}

