package kr.hhplus.be.server.interfaces.api.coupon.dto;

import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;

import java.time.LocalDateTime;

// 레코드는 자동으로 모든 필드에 대한 생성자, 접근자 메서드, equals(), hashCode(), toString() 메서드를 생성
public record IssuedCouponResponse (
        Long id,
        Long couponId,
        String name,
        String discountType,
        int discountAmt,
        LocalDateTime issuedAt,
        LocalDateTime validStartedAt,
        LocalDateTime validEndedAt,
        LocalDateTime usedAt
) {
    public static IssuedCouponResponse of(IssuedCoupon issuedCoupon) {
        return new IssuedCouponResponse(
                issuedCoupon.getId(),
                issuedCoupon.getCoupon().getId(),
                issuedCoupon.getName(),
                issuedCoupon.getDiscountType().name(),
                issuedCoupon.getDiscountAmt(),
                issuedCoupon.getIssuedAt(),
                issuedCoupon.getValidStartedAt(),
                issuedCoupon.getValidEndedAt(),
                issuedCoupon.getUsedAt()
        );
    }
}
