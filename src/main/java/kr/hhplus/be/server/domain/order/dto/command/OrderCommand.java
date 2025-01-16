package kr.hhplus.be.server.domain.order.dto.command;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

public class OrderCommand {
    public record Order(
            Long userId,
            List<OrderDetail> details
    ) {
    }

    public record OrderDetail(
            Long productId,
            int quantity,
            BigDecimal price
    ) {
        @Builder
        public OrderDetail {}
    }
}