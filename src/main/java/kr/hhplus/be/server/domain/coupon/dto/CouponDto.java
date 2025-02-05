package kr.hhplus.be.server.domain.coupon.dto;

import lombok.Getter;

@Getter
public class CouponDto {
    private long couponId;
    private long userId;
    private long currentMillis;

    public CouponDto(long couponId, long userId, long currentMillis) {
        this.couponId = couponId;
        this.userId = userId;
        this.currentMillis = currentMillis;
    }
}
