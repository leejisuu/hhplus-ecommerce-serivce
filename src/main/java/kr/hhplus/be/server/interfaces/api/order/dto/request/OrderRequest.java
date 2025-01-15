package kr.hhplus.be.server.interfaces.api.order.dto.request;

import kr.hhplus.be.server.application.order.dto.criteria.OrderCriteria;
import kr.hhplus.be.server.application.order.dto.criteria.OrderDetailCriteria;

import java.util.List;
import java.util.stream.Collectors;

public record OrderRequest(
         Long userId,
         List<OrderDetailRequest> orderDetails
) {
    public OrderCriteria toCriteria() {
        List<OrderDetailCriteria> orderDetailCriterias = this.orderDetails.stream()
                .map(OrderDetailRequest::toCriteria)
                .collect(Collectors.toList());

        return new OrderCriteria(userId, orderDetailCriterias);
    }
}

