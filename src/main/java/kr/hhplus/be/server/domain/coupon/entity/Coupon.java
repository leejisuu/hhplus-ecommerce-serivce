package kr.hhplus.be.server.domain.coupon.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import kr.hhplus.be.server.domain.coupon.enums.CouponStatus;
import kr.hhplus.be.server.domain.coupon.enums.DiscountType;
import kr.hhplus.be.server.domain.coupon.enums.IssuedCouponStatus;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, columnDefinition = "VARCHAR(20)")
    private DiscountType discountType;

    @Column(name = "discount_amt", nullable = false)
    private BigDecimal discountAmt;

    @Column(name = "max_capacity", nullable = false)
    private int maxCapacity;

    @Column(name = "remain_capacity", nullable = false)
    private int remainCapacity;

    @Column(name = "valid_started_at", nullable = false)
    private LocalDateTime validStartedAt;

    @Column(name = "valid_ended_at", nullable = false)
    private LocalDateTime validEndedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR(20)")
    private CouponStatus status;

    @Builder
    private Coupon(String name, DiscountType discountType, BigDecimal discountAmt, int maxCapacity, int remainCapacity, LocalDateTime validStartedAt, LocalDateTime validEndedAt, CouponStatus status) {
        this.name = name;
        this.discountType = discountType;
        this.discountAmt = discountAmt;
        this.maxCapacity = maxCapacity;
        this.remainCapacity = remainCapacity;
        this.validStartedAt = validStartedAt;
        this.validEndedAt = validEndedAt;
        this.status = status;
    }

    public static Coupon create(String name, DiscountType discountType, BigDecimal discountAmt, int maxCapacity, int remainCapacity, LocalDateTime validStartedAt, LocalDateTime validEndedAt, CouponStatus status) {
        return Coupon.builder()
                .name(name)
                .discountType(discountType)
                .discountAmt(discountAmt)
                .maxCapacity(maxCapacity)
                .remainCapacity(remainCapacity)
                .validStartedAt(validStartedAt)
                .validEndedAt(validEndedAt)
                .status(status)
                .build();
    }

    public IssuedCoupon issue(Long userId, LocalDateTime issuedAt) {
        // 쿠폰 마스터 상태가 "DEACTIVATEDE" 라면 예외 발생.
        if(this.status == CouponStatus.DEACTIVATED) {
            throw new CustomException(ErrorCode.DEACTIVATED_COUPON);
        }

        // 쿠폰의 유효 기간이 만료 됐다면 예외 발생.
        if(issuedAt.isBefore(this.validStartedAt) || !issuedAt.isBefore(this.validEndedAt)) {
            throw new CustomException(ErrorCode.COUPON_EXPIRED);
        }

        // 쿠폰이 모두 소진 됐다면 예외 발생.
        if(this.remainCapacity <= 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_COUPON_QUANTITY);
        }

        // 쿠폰 잔여 개수 1개 차감
        this.remainCapacity = this.remainCapacity - 1;

        return IssuedCoupon.builder()
                .couponId(this.id)
                .userId(userId)
                .name(this.name)
                .discountType(this.discountType)
                .discountAmt(this.discountAmt)
                .issuedAt(issuedAt)
                .validStartedAt(this.validStartedAt)
                .validEndedAt(this.validEndedAt)
                .usedAt(null)
                .status(IssuedCouponStatus.UNUSED)
                .build();
    }

    public void decreaseRemainCapacity() {
        this.remainCapacity--;
    }
}
