package kr.hhplus.be.server.application.order.dto.result;

import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;

import java.math.BigDecimal;

public class OrderResult {

    public record Create(
            String orderNo,
            BigDecimal totalOriginalAmt,
            BigDecimal discountAmt,
            BigDecimal finalPaymentAmt
    ) {
        public static Create of(OrderInfo.Create orderInfo) {
            return new Create(
                    orderInfo.orderNo(),
                    orderInfo.totalOriginalAmt(),
                    orderInfo.discountAmt(),
                    orderInfo.finalPaymentAmt()
            );
        }
    }
}