package kr.hhplus.be.server.domain.coupon.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.DiscountType;
import kr.hhplus.be.server.domain.coupon.IssuedCouponStatus;
import kr.hhplus.be.server.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class IssuedCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Column(name = "coupon_name")
    private String name;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private int discountAmt;

    private LocalDateTime issuedAt;

    private LocalDateTime validStartedAt;

    private LocalDateTime validEndedAt;

    private LocalDateTime usedAt;

    private IssuedCouponStatus status;

    @Builder
    public IssuedCoupon(User user, Coupon coupon, String name, DiscountType discountType, int discountAmt, LocalDateTime issuedAt, LocalDateTime validStartedAt, LocalDateTime validEndedAt, LocalDateTime usedAt, IssuedCouponStatus status) {
        this.user = user;
        this.coupon = coupon;
        this.name = name;
        this.discountType = discountType;
        this.discountAmt = discountAmt;
        this.issuedAt = issuedAt;
        this.validStartedAt = validStartedAt;
        this.validEndedAt = validEndedAt;
        this.usedAt = usedAt;
        this.status = status;
    }
}