package kr.hhplus.be.server.application.payment.dto.result;

import kr.hhplus.be.server.domain.payment.dto.info.PaymentInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResult {
    
    public record Payment(
            Long id,
            Long orderId,
            String status,
            BigDecimal totalOriginalAmt,
            BigDecimal discountAmt,
            BigDecimal finalPaymentAmt,
            LocalDateTime createdAt
    ) {
        public static PaymentResult.Payment of(PaymentInfo.PaymentDto info) {
            return new PaymentResult.Payment(
                    info.id(),
                    info.orderId(),
                    info.status(),
                    info.totalOriginalAmt(),
                    info.discountAmt(),
                    info.finalPaymentAmt(),
                    info.createdAt()
            );
        }
    }
}
