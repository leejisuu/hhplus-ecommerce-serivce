package kr.hhplus.be.server.domain.coupon.dto.info;

import kr.hhplus.be.server.domain.coupon.entity.Coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CouponInfo {

    public record Create(
            Long id,
            String name,
            String discountType,
            BigDecimal discountAmt,
            int maxCapacity,
            int remainCapacity,
            LocalDateTime validStartedAt,
            LocalDateTime validEndedAt,
            String status
    ) {
        public static CouponInfo.Create of(Coupon coupon) {
            return new CouponInfo.Create (
                    coupon.getId(),
                    coupon.getName(),
                    coupon.getDiscountType().name(),
                    coupon.getDiscountAmt(),
                    coupon.getMaxCapacity(),
                    coupon.getRemainCapacity(),
                    coupon.getValidStartedAt(),
                    coupon.getValidEndedAt(),
                    coupon.getStatus().name()
            );
        }
    }
}
