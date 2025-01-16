package kr.hhplus.be.server.domain.payment.dto.info;

import kr.hhplus.be.server.domain.payment.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentInfo {

    public record PaymentDto(
            Long id,
            Long orderId,
            String status,
            BigDecimal totalOriginalAmt,
            BigDecimal discountAmt,
            BigDecimal finalPaymentAmt,
            LocalDateTime createdAt
    ) {
        public static PaymentInfo.PaymentDto of(Payment payment) {
            return new PaymentInfo.PaymentDto(
                    payment.getId(),
                    payment.getOrderId(),
                    payment.getStatus().name(),
                    payment.getTotalOriginalAmt(),
                    payment.getDiscountAmt(),
                    payment.getFinalPaymentAmt(),
                    payment.getCreatedAt()
            );
        }


    }
}
