package kr.hhplus.be.server.domain.coupon.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.coupon.DiscountType;
import kr.hhplus.be.server.domain.coupon.IssuedCouponStatus;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.support.exception.CustomException;
import kr.hhplus.be.server.support.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private int discountAmt;

    private int maxCapacity;

    private LocalDateTime validStartedAt;

    private LocalDateTime validEndedAt;

    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    @OneToMany(mappedBy = "coupon")
    private List<IssuedCoupon> issuedCoupons;

    public IssuedCoupon makeIssuedCoupon(User user, LocalDateTime issuedAt) {
        // 쿠폰 상태가 "ACTIVE"인지 확인
        if(this.status.equals(CouponStatus.DEACTIVATED)) {
            throw new CustomException(ErrorCode.DEACTIVATED_COUPON);
        }

        // 쿠폰이 모두 소진됐는지 체크
        if(this.issuedCoupons.size() > this.maxCapacity) {
            throw new CustomException(ErrorCode.INSUFFICIENT_COUPON_QUANTITY);
        }

        // 이미 발급 받은 쿠폰인지 체크
        if(this.issuedCoupons.stream()
               .anyMatch(cp -> cp.getUser().getId().equals(user.getId()))) {
           throw new CustomException(ErrorCode.ALREADY_ISSUED_COUPON);
       }

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
