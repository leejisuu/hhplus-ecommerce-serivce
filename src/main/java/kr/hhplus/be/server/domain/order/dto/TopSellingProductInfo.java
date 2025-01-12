package kr.hhplus.be.server.domain.order.dto;

public record TopSellingProductInfo(
        Long productId,
        String name,
        int quantity,
        int price,
        Long totalSales
) {
}
