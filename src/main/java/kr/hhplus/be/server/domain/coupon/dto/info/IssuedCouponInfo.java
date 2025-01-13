package kr.hhplus.be.server.domain.coupon.dto.info;

import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IssuedCouponInfo(
        Long id,
        String name,
        String discountType,
        BigDecimal discountAmt,
        LocalDateTime issuedAt,
        LocalDateTime validStartedAt,
        LocalDateTime validEndedAt,
        LocalDateTime usedAt
) {
    public static IssuedCouponInfo from(IssuedCoupon issuedCoupon) {
        return new IssuedCouponInfo(
                issuedCoupon.getId(),
                issuedCoupon.getCoupon().getName(),
                issuedCoupon.getCoupon().getDiscountType().name(),
                issuedCoupon.getCoupon().getDiscountAmt(),
                issuedCoupon.getIssuedAt(),
                issuedCoupon.getCoupon().getValidStartedAt(),
                issuedCoupon.getCoupon().getValidEndedAt(),
                issuedCoupon.getUsedAt()
        );
    }
}
