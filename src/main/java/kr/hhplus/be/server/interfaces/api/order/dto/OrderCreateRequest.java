package kr.hhplus.be.server.interfaces.api.order.dto;

import kr.hhplus.be.server.application.order.dto.criteria.OrderCreateCriteria;
import kr.hhplus.be.server.application.order.dto.criteria.OrderDetailCriteria;

import java.util.List;
import java.util.stream.Collectors;

public record OrderCreateRequest (
         Long userId,
         List<OrderDetailRequest> productRequests,
         Long couponId
) {
    public OrderCreateCriteria toOrderCreateCriteria() {
        List<OrderDetailCriteria> orderDetailCriteriaList = this.productRequests.stream()
                .map(OrderDetailRequest::toCriteria)
                .collect(Collectors.toList());

        return new OrderCreateCriteria(userId, orderDetailCriteriaList, couponId);
    }
}

