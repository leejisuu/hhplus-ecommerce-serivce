package kr.hhplus.be.server.domain.order.dto.command;

public record OrderDetailCommand(
        Long productId,
        int quantity
) {
}
