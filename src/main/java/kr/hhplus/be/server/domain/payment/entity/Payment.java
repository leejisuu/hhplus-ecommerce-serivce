package kr.hhplus.be.server.domain.payment.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import kr.hhplus.be.server.domain.payment.enums.PaymentStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", nullable = false)
    private String orderNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR(20)")
    private PaymentStatus status;

    @Column(name = "payment_amt", nullable = false)
    private BigDecimal paymentAmt;

    @Builder
    private Payment(String orderNo, PaymentStatus status, BigDecimal paymentAmt) {
        this.orderNo = orderNo;
        this.status = status;
        this.paymentAmt = paymentAmt;
    }

    public static Payment create(String orderNo, BigDecimal paymentAmt) {
        return Payment.builder()
                .orderNo(orderNo)
                .status(PaymentStatus.COMPLETED)
                .paymentAmt(paymentAmt)
                .build();
    }
}
