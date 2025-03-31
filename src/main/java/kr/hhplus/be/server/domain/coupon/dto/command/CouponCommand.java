package kr.hhplus.be.server.domain.coupon.dto.command;

import kr.hhplus.be.server.domain.coupon.dto.CouponDto;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.enums.CouponStatus;
import kr.hhplus.be.server.domain.coupon.enums.DiscountType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CouponCommand {

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

    public record Issue(
            Long couponId,
            Long userId,
            LocalDateTime currentDateTime
    ) {
        @Builder
        public Issue {}

        public static CouponCommand.Issue of(CouponDto couponDto) {
            return new CouponCommand.Issue(
                    couponDto.getCouponId(),
                    couponDto.getUserId(),
                    LocalDateTime.now()
            );
        }
    }

    public record AddQueue(
            Long couponId,
            Long userId,
            Long currentMillis
    ) {
        public CouponDto toCouponDto() {
            return new CouponDto(couponId, userId, currentMillis);
        }
    }
}
