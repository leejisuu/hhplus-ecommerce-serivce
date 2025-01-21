package kr.hhplus.be.server.domain.payment.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import kr.hhplus.be.server.domain.order.entity.Order;
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

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @Column(name = "total_original_amt", nullable = false)
    private BigDecimal totalOriginalAmt;

    @Column(name = "discount_amt", nullable = false)
    private BigDecimal discountAmt;

    @Column(name = "final_payment_amt", nullable = false)
    private BigDecimal finalPaymentAmt;

    @Column(name = "issued_coupon_id")
    private Long issuedCouponId;

    @Builder
    private Payment(Long orderId, PaymentStatus status, BigDecimal totalOriginalAmt, BigDecimal discountAmt, BigDecimal finalPaymentAmt, Long issuedCouponId) {
        this.orderId = orderId;
        this.status = status;
        this.totalOriginalAmt = totalOriginalAmt;
        this.discountAmt = discountAmt;
        this.finalPaymentAmt = finalPaymentAmt;
        this.issuedCouponId = issuedCouponId;
    }

    public static Payment create(Long orderId, BigDecimal totalOriginalAmt, BigDecimal discountAmt, Long issuedCouponId) {
        return Payment.builder()
                .orderId(orderId)
                .status(PaymentStatus.IN_PROGRESS)
                .totalOriginalAmt(totalOriginalAmt)
                .discountAmt(discountAmt)
                .finalPaymentAmt(totalOriginalAmt.subtract(discountAmt))
                .issuedCouponId(issuedCouponId)
                .build();
    }

    public void fail() {
        this.status = PaymentStatus.FAILED;
    }

    public void complete() {
        this.status = PaymentStatus.COMPLETED;
    }
}
