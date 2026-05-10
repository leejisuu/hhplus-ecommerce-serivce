package kr.hhplus.be.server.interfaces.api.payment.dto;

import kr.hhplus.be.server.application.payment.dto.result.PaymentResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {
    public record Payment(
            Long id,
            String orderNo,
            String status,
            BigDecimal paymentAmt,
            LocalDateTime createdAt
    ) {
        public static PaymentResponse.Payment of(PaymentResult.Payment result) {
            return new PaymentResponse.Payment(
                    result.id(),
                    result.orderNo(),
                    result.status(),
                    result.paymentAmt(),
                    result.createdAt()
            );
        }
        
        
    }
}
