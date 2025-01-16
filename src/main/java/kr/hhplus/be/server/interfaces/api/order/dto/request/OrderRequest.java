package kr.hhplus.be.server.interfaces.api.order.dto.request;

import kr.hhplus.be.server.application.order.dto.criteria.OrderCriteria;
import kr.hhplus.be.server.application.order.dto.criteria.OrderDetailCriteria;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {

    public record Order(
            Long userId,
            List<OrderDetail> details
    ) {

        public OrderCriteria.Order toCriteria() {
            List<OrderCriteria.OrderDetail> criteriaOrderDetails = details.stream()
                    .map(orderDetail -> new OrderCriteria.OrderDetail(
                            orderDetail.productId(),
                            orderDetail.quantity()
                    ))
                    .collect(Collectors.toList());

            return new OrderCriteria.Order(userId, criteriaOrderDetails);
        }
    }

    public record OrderDetail(
            Long productId,
            int quantity
    ) {

    }
}

