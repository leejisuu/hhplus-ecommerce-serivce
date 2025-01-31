package kr.hhplus.be.server.application.order.dto.criteria;

import lombok.Builder;

import java.util.List;

public class OrderCriteria {
    public record Order(
            Long userId,
            List<OrderDetail> details
    ) {

        @Builder
        public Order {}
    }

    public record OrderDetail(
            Long productId,
            int quantity
    ) {
        @Builder
        public OrderDetail {}
    }
}
