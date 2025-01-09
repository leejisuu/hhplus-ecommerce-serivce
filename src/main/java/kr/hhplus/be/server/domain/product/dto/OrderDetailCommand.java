package kr.hhplus.be.server.domain.product.dto;

public record OrderDetailCommand(
        Long productId,
        int quantity
) {
}
