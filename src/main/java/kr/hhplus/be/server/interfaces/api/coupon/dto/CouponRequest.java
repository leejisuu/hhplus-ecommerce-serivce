package kr.hhplus.be.server.interfaces.api.coupon.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CouponRequest {
    private Long userId;
    private Long couponId;

    @Builder
    public CouponRequest(Long userId, Long couponId) {
        this.userId = userId;
        this.couponId = couponId;
    }
}
