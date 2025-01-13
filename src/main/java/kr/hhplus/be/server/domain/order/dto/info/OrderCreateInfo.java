package kr.hhplus.be.server.domain.order.dto.info;

import kr.hhplus.be.server.domain.order.entity.Order;

import java.math.BigDecimal;

public record OrderCreateInfo(
        Long id,
        BigDecimal netAmt,
        BigDecimal discountAmt,
        BigDecimal totalAmt,
        Long couponId
) {
    public static OrderCreateInfo from(Order order) {
        return new OrderCreateInfo(
                order.getId(),
                order.getNetAmt(),
                order.getDiscountAmt(),
                order.getTotalAmt(),
                order.getIssuedCoupon().getId()
        );
    }
}
