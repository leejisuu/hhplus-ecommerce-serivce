package kr.hhplus.be.server.interfaces.api.order.dto.response;

import kr.hhplus.be.server.application.order.dto.result.OrderResult;

import java.math.BigDecimal;

public class OrderResponse {

    public record Create(
            String orderNo,
            BigDecimal totalOriginalAmt,
            BigDecimal discountAmt,
            BigDecimal finalPaymentAmt
    ) {
        public static Create of(OrderResult.Create createResult) {
            return new Create(
                    createResult.orderNo(),
                    createResult.totalOriginalAmt(),
                    createResult.discountAmt(),
                    createResult.finalPaymentAmt()
            );
        }
    }
}

