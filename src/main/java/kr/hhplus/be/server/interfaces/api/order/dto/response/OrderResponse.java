package kr.hhplus.be.server.interfaces.api.order.dto.response;

import kr.hhplus.be.server.application.order.dto.result.OrderResult;

import java.math.BigDecimal;

public record OrderResponse(
        Long id,
        BigDecimal totalOriginalAmt
) {

    public static OrderResponse from(OrderResult OrderResult) {

        return new OrderResponse(
                OrderResult.id(),
                OrderResult.totalOriginalAmt()
        );
    }
}

