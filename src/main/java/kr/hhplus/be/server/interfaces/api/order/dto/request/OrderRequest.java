package kr.hhplus.be.server.interfaces.api.order.dto.request;

import kr.hhplus.be.server.application.order.dto.criteria.OrderCriteria;

import java.util.List;

public class OrderRequest {

    public record Create(
            Long userId,
            Long couponId,
            List<OrderDetail> details
    ) {

        public OrderCriteria.Create toCriteria() {
            List<OrderCriteria.OrderDetail> criteriaOrderDetails = details.stream()
                    .map(orderDetail -> new OrderCriteria.OrderDetail(
                            orderDetail.productId(),
                            orderDetail.quantity()
                    ))
                    .toList();

            return new OrderCriteria.Create(userId, couponId, criteriaOrderDetails);
        }
    }

    public record OrderDetail(
            Long productId,
            int quantity
    ) {

    }
}

