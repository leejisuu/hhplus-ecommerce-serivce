package kr.hhplus.be.server.domain.coupon.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import kr.hhplus.be.server.domain.coupon.enums.CouponStatus;
import kr.hhplus.be.server.domain.coupon.enums.DiscountType;
import kr.hhplus.be.server.domain.coupon.enums.IssuedCouponStatus;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.support.exception.CustomException;
import kr.hhplus.be.server.support.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private int discountAmt;

    private int maxCapacity;

    private int remainCapacity;

    private LocalDateTime validStartedAt;

    private LocalDateTime validEndedAt;

    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    @Builder
    public Coupon(String name, DiscountType discountType, int discountAmt, int maxCapacity, int remainCapacity, LocalDateTime validStartedAt, LocalDateTime validEndedAt, CouponStatus status) {
        this.name = name;
        this.discountType = discountType;
        this.discountAmt = discountAmt;
        this.maxCapacity = maxCapacity;
        this.remainCapacity = remainCapacity;
        this.validStartedAt = validStartedAt;
        this.validEndedAt = validEndedAt;
        this.status = status;
    }

    public IssuedCoupon makeIssuedCoupon(User user, LocalDateTime issuedAt) {
        // 쿠폰 상태가 "DEACTIVATEDE" 라면 예외 발생.
        if(this.status == CouponStatus.DEACTIVATED) {
            throw new CustomException(ErrorCode.DEACTIVATED_COUPON);
        }

        // 쿠폰의 유효 기간이 만료 됐다면 예외 발생.
        if(!issuedAt.isBefore(this.validEndedAt) || issuedAt.isEqual(this.validStartedAt)) {
            throw new CustomException(ErrorCode.COUPON_EXPIRED);
        }

        // 쿠폰이 모두 소진 됐다면 예외 발생.
        if(this.remainCapacity <= 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_COUPON_QUANTITY);
        }

        // 쿠폰 잔여 개수 1개 차감
        this.remainCapacity--;

        return IssuedCoupon.builder()
                .user(user)
                .coupon(this)
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
}
