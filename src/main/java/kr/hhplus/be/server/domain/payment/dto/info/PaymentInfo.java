package kr.hhplus.be.server.domain.payment.dto.info;

import kr.hhplus.be.server.domain.payment.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentInfo {

    public record PaymentDto(
            Long id,
            String orderNo,
            String status,
            BigDecimal paymentAmt,
            LocalDateTime createdAt
    ) {
        public static PaymentInfo.PaymentDto of(Payment payment) {
            return new PaymentInfo.PaymentDto(
                    payment.getId(),
                    payment.getOrderNo(),
                    payment.getStatus().name(),
                    payment.getPaymentAmt(),
                    payment.getCreatedAt()
            );
        }


    }
}
