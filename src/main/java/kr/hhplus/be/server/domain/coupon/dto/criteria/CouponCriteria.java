package kr.hhplus.be.server.domain.coupon.dto.criteria;

import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.enums.CouponStatus;
import kr.hhplus.be.server.domain.coupon.enums.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CouponCriteria {

    public record Create (
            String name,
            DiscountType discountType,
            BigDecimal discountAmt,
            int maxCapacity,
            int remainCapacity,
            LocalDateTime validStartedAt,
            LocalDateTime validEndedAt,
            CouponStatus status
    ) {
        public Coupon createCoupon() {
            return Coupon.create(name, discountType, discountAmt, maxCapacity, remainCapacity, validStartedAt, validEndedAt, status);
        }
    }
}
