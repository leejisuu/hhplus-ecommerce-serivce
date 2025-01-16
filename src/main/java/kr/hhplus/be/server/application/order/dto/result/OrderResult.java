package kr.hhplus.be.server.application.order.dto.result;

import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;

import java.math.BigDecimal;

public class OrderResult {

    public record Order(
            Long id,
            BigDecimal totalOriginalAmt
    ) {
        public static OrderResult.Order of(OrderInfo.OrderDto orderInfo) {

            return new OrderResult.Order(
                    orderInfo.id(),
                    orderInfo.totalOriginalAmt()
            );
        }
    }
}