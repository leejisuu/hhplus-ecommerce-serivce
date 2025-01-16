package kr.hhplus.be.server.domain.coupon.dto.info;

import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class IssuedCouponInfo {
    public record Coupon(
            Long id,
            String name,
            String discountType,
            BigDecimal discountAmt,
            LocalDateTime issuedAt,
            LocalDateTime validStartedAt,
            LocalDateTime validEndedAt,
            LocalDateTime usedAt
    ) {
        public static IssuedCouponInfo.Coupon of(IssuedCoupon issuedCoupon) {
            return new IssuedCouponInfo.Coupon (
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
}