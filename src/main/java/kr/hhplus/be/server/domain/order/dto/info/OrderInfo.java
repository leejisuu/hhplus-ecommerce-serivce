package kr.hhplus.be.server.domain.order.dto.info;

import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.order.enums.OrderStatus;

import java.math.BigDecimal;

public class OrderInfo {

    public record Create(
            String orderNo,
            BigDecimal totalOriginalAmt,
            BigDecimal discountAmt,
            BigDecimal finalPaymentAmt
    ) {
        public static Create of(Order order) {
            return new Create(
                    order.getOrderNo(),
                    order.getTotalOriginalAmt(),
                    order.getDiscountAmt(),
                    order.getFinalPaymentAmt()
            );
        }
    }

    public record Detail(
            String orderNo,
            OrderStatus status,
            BigDecimal totalOriginalAmt,
            BigDecimal discountAmt,
            BigDecimal finalPaymentAmt
    ) {
        public static Detail of(Order order) {
            return new Detail(
                    order.getOrderNo(),
                    order.getStatus(),
                    order.getTotalOriginalAmt(),
                    order.getDiscountAmt(),
                    order.getFinalPaymentAmt()
            );
        }
    }

    public record Payment(
            String orderNo,
            BigDecimal paymentAmt
    ) {
        public static Payment of(Order order) {
            return new Payment(
                    order.getOrderNo(),
                    order.getFinalPaymentAmt()
            );
        }
    }

    public record Confirm(
            String orderNo,
            Long couponId,
            BigDecimal paymentAmt
    ) {
        public static Confirm of(Order order) {
            return new Confirm(
                    order.getOrderNo(),
                    order.getUsedCouponId(),
                    order.getFinalPaymentAmt()
            );
        }
    }
}