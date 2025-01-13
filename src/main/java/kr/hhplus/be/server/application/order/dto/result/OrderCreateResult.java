package kr.hhplus.be.server.application.order.dto.result;

import kr.hhplus.be.server.domain.order.dto.info.OrderCreateInfo;

import java.math.BigDecimal;

public record OrderCreateResult(
        Long id,
        BigDecimal netAmt,
        BigDecimal discountAmt,
        BigDecimal totalAmt,
        Long couponId
) {

    public static OrderCreateResult from(OrderCreateInfo orderCreateInfo) {
        return new OrderCreateResult(
                orderCreateInfo.id(),
                orderCreateInfo.netAmt(),
                orderCreateInfo.discountAmt(),
                orderCreateInfo.totalAmt(),
                orderCreateInfo.couponId()
        );
    }
}
