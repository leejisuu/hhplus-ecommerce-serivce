package kr.hhplus.be.server.interfaces.api.coupon.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CouponIssueRequest {
    private Long userId;
    private Long couponId;

    @Builder
    public CouponIssueRequest(Long userId, Long couponId) {
        this.userId = userId;
        this.couponId = couponId;
    }
}
