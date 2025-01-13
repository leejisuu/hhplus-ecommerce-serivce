package kr.hhplus.be.server.interfaces.api.order.dto;

import kr.hhplus.be.server.application.order.dto.result.OrderCreateResult;

import java.math.BigDecimal;

public record OrderCreateResponse (
        Long id,
        BigDecimal netAmt,
        BigDecimal discountAmt,
        BigDecimal totalAmt,
        Long couponId
) {

    public static OrderCreateResponse from(OrderCreateResult orderCreateResult) {

        return new OrderCreateResponse(
                orderCreateResult.id(),
                orderCreateResult.netAmt(),
                orderCreateResult.discountAmt(),
                orderCreateResult.totalAmt(),
                orderCreateResult.couponId()
        );
    }
}

