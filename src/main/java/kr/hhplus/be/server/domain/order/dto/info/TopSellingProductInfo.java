package kr.hhplus.be.server.domain.order.dto.info;

import java.math.BigDecimal;

public record TopSellingProductInfo(
        Long productId,
        String name,
        int quantity,
        BigDecimal price,
        Long totalSales
) {
}
