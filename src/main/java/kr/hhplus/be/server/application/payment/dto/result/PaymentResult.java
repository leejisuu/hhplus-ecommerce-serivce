package kr.hhplus.be.server.application.payment.dto.result;

import kr.hhplus.be.server.domain.payment.dto.info.PaymentInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResult {
    
    public record Payment(
            Long id,
            String orderNo,
            String status,
            BigDecimal paymentAmt,
            LocalDateTime createdAt
    ) {
        public static PaymentResult.Payment of(PaymentInfo.PaymentDto info) {
            return new PaymentResult.Payment(
                    info.id(),
                    info.orderNo(),
                    info.status(),
                    info.paymentAmt(),
                    info.createdAt()
            );
        }
    }
}
