package kr.hhplus.be.server.application.order.dto.criteria;

import kr.hhplus.be.server.domain.order.dto.command.OrderCommand;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrderCriteria {
    public record Create(
            Long userId,
            Long couponId,
            List<OrderDetail> details
    ) {

        @Builder
        public Create {}

        public OrderCommand.Create toCommand(String orderNo,
                                             Map<Long, BigDecimal> priceMap,
                                             BigDecimal totalOriginalAmt,
                                             BigDecimal discountAmt) {
            List<OrderCommand.OrderDetail> orderDetails = this.details().stream()
                    .map(orderDetail -> new OrderCommand.OrderDetail(orderDetail.productId(), orderDetail.quantity(), priceMap.get(orderDetail.productId())))
                    .collect(Collectors.toList());

            return new OrderCommand.Create(orderNo, this.userId(), this.couponId(), orderDetails, totalOriginalAmt, discountAmt);
        }

        public List<Long> getProductIds() {
            return this.details().stream().map(OrderCriteria.OrderDetail::productId).collect(Collectors.toList());
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
