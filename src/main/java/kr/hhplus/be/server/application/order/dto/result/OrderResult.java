package kr.hhplus.be.server.application.order.dto.result;

import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;

import java.math.BigDecimal;

public record OrderResult (
        Long id,
        BigDecimal totalOriginalAmt
) {

    public static OrderResult from(OrderInfo orderInfo) {
        return new OrderResult(
                orderInfo.id(),
                orderInfo.totalOrginalAmt()
        );
    }
}
