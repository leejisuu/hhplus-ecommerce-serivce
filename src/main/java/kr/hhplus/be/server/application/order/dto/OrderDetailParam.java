package kr.hhplus.be.server.application.order.dto;

import kr.hhplus.be.server.domain.product.dto.OrderDetailCommand;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderDetailRequest;

public record OrderDetailParam(
        Long productId,
        int quantity
) {
    public static OrderDetailParam of(OrderDetailRequest orderDetailRequest) {
        return new OrderDetailParam(
                orderDetailRequest.productId(),
                orderDetailRequest.quantity()
        );
    }

    public OrderDetailCommand toCommand() {
        return new OrderDetailCommand(productId, quantity);
    }
}
