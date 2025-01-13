package kr.hhplus.be.server.interfaces.api.order.dto;

import kr.hhplus.be.server.application.order.dto.criteria.OrderDetailCriteria;

public record OrderDetailRequest (
        Long productId,
        int quantity
) {
    public OrderDetailCriteria toCriteria() {
        return new OrderDetailCriteria(
                productId,
                quantity
        );
    }
}

