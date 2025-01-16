package kr.hhplus.be.server.application.order.dto.criteria;

import kr.hhplus.be.server.domain.product.dto.StockCommand;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

public class OrderCriteria {
    public record Order(
            Long userId,
            List<OrderDetail> details
    ) {

        @Builder
        public Order {}

        public StockCommand.OrderDetails toStockCommand() {
            List<StockCommand.OrderDetail> orderDetils = details.stream()
                    .map(orderDetail -> {
                        return StockCommand.OrderDetail.builder()
                                .productId(orderDetail.productId)
                                .quantity(orderDetail.quantity)
                                .build();
                    })
                    .collect(Collectors.toList());

            return new StockCommand.OrderDetails(orderDetils);
        }
    }

    public record OrderDetail(
            Long productId,
            int quantity
    ) {
        @Builder
        public OrderDetail {}
    }
}
