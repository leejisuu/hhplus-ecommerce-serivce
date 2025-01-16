package kr.hhplus.be.server.interfaces.api.order.dto.response;

import kr.hhplus.be.server.application.order.dto.result.OrderResult;

import java.math.BigDecimal;

public class OrderResponse {

    public record Order(
            Long id,
            BigDecimal totalOriginalAmt
    ) {
        public static OrderResponse.Order of(OrderResult.Order orderResult) {

            return new OrderResponse.Order(
                    orderResult.id(),
                    orderResult.totalOriginalAmt()
            );
        }
    }
}

