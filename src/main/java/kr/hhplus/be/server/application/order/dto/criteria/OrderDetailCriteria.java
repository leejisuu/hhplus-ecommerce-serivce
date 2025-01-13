package kr.hhplus.be.server.application.order.dto.criteria;

import kr.hhplus.be.server.domain.order.dto.command.OrderDetailCommand;

public record OrderDetailCriteria(
        Long productId,
        int quantity
) {
    public OrderDetailCommand toCommand() {
        return new OrderDetailCommand(productId, quantity);
    }
}
