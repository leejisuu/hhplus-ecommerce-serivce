package kr.hhplus.be.server.domain.order.event;

import kr.hhplus.be.server.domain.order.entity.Order;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderEvent {

    public record Confirmed(
            String eventId,
            String orderNo,
            Long couponId,
            BigDecimal paymentAmt
    ) {
        public static Confirmed of(Order order) {
            return new Confirmed(UUID.randomUUID().toString(), order.getOrderNo(), order.getUsedCouponId(), order.getFinalPaymentAmt());
        }
    }
}
