package kr.hhplus.be.server.interfaces.api.payment.dto;

import kr.hhplus.be.server.application.payment.dto.result.PaymentResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {
    public record Payment(
            Long id,
            Long orderId,
            String status,
            BigDecimal totalOriginalAmt,
            BigDecimal discountAmt,
            BigDecimal finalPaymentAmt,
            LocalDateTime createdAt
    ) {
        public static PaymentResponse.Payment of(PaymentResult.Payment result) {
            return new PaymentResponse.Payment(
                    result.id(),
                    result.orderId(),
                    result.status(),
                    result.totalOriginalAmt(),
                    result.discountAmt(),
                    result.finalPaymentAmt(),
                    result.createdAt()
            );
        }
        
        
    }
}
