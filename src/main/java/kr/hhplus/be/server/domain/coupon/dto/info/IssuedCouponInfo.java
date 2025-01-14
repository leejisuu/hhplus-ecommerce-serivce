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
    public static IssuedCouponInfo of(IssuedCoupon issuedCoupon) {
        return new IssuedCouponInfo(
                issuedCoupon.getId(),
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
