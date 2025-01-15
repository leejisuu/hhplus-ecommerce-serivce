package kr.hhplus.be.server.domain.order.dto.info;

import kr.hhplus.be.server.domain.order.entity.Order;

import java.math.BigDecimal;

public record OrderInfo(
        Long id,
        BigDecimal totalOrginalAmt
) {
    public static OrderInfo of(Order order) {
        return new OrderInfo(
                order.getId(),
                order.getTotalOriginalAmt()
        );
    }
}
