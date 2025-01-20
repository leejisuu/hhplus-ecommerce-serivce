package kr.hhplus.be.server.domain.coupon.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
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
@Table(name = "issued_coupon")
public class IssuedCoupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_id", nullable = false)
    private Long couponId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "coupon_name", nullable = false)
    private String name;

    @Column(name = "discount_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column(name = "discount_amt", nullable = false)
    private BigDecimal discountAmt;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(name = "valid_started_at", nullable = false)
    private LocalDateTime validStartedAt;

    @Column(name = "valid_ended_at", nullable = false)
    private LocalDateTime validEndedAt;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private IssuedCouponStatus status;

    @Builder
    private IssuedCoupon(Long couponId, Long userId, String name, DiscountType discountType, BigDecimal discountAmt,
                        LocalDateTime issuedAt, LocalDateTime validStartedAt, LocalDateTime validEndedAt, LocalDateTime usedAt, IssuedCouponStatus status) {
        this.couponId = couponId;
        this.userId = userId;
        this.name = name;
        this.discountType = discountType;
        this.discountAmt = discountAmt;
        this.issuedAt = issuedAt;
        this.validStartedAt = validStartedAt;
        this.validEndedAt = validEndedAt;
        this.usedAt = usedAt;
        this.status = status;
    }

    public BigDecimal use(BigDecimal totalOriginalPrice, LocalDateTime usedAt) {
        BigDecimal discountAmt;

        if(discountType.equals(DiscountType.PERCENTAGE)) {
            discountAmt = (totalOriginalPrice.multiply(this.discountAmt)).divide(new BigDecimal(100));
        } else {
            discountAmt = this.discountAmt;
        }

        if(totalOriginalPrice.subtract(discountAmt).compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomException(ErrorCode.COUPON_DISCOUNT_EXCEEDS_NET_AMOUNT);
        }

        this.status = IssuedCouponStatus.USED;
        this.usedAt = usedAt;

        return discountAmt;
    }
}
