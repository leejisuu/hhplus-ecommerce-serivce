package kr.hhplus.be.server.domain.order.dto.command;

import kr.hhplus.be.server.domain.order.entity.Order;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

public class OrderCommand {
    public record Create(
            String orderNo,
            Long userId,
            Long couponId,
            List<OrderDetail> details,
            BigDecimal totalOriginalAmt,
            BigDecimal discountAmt
    ) {
        public Order toEntity() {
            return Order.create(
                    orderNo, userId, totalOriginalAmt, discountAmt, couponId
            );
        }
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