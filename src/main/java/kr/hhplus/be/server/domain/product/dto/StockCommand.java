package kr.hhplus.be.server.domain.product.dto;

import lombok.Builder;

import java.util.List;

public class StockCommand {

    public record OrderDetails(
        List<OrderDetail> details
    ) {

    }

    public record OrderDetail(
        Long productId,
        int quantity
    ) {
        @Builder
        public OrderDetail {}
    }
}
