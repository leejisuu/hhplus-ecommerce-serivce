package kr.hhplus.be.server.interfaces.api.coupon.dto;


import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import lombok.Builder;

import java.time.LocalDateTime;

public class IssuedCouponResponse {
    private Long id;
    private Long couponId;
    private String name;
    private String discountType;
    private int discountAmt;
    private LocalDateTime issuedAt;
    private LocalDateTime validStartedAt;
    private LocalDateTime validEndedAt;
    private LocalDateTime usedAt;
    private String status;

    @Builder
    public IssuedCouponResponse(Long id, Long couponId, String name, String discountType, int discountAmt, LocalDateTime issuedAt, LocalDateTime validStartedAt, LocalDateTime validEndedAt, LocalDateTime usedAt, String status) {
        this.id = id;
        this.couponId = couponId;
        this.name = name;
        this.discountType = discountType;
        this.discountAmt = discountAmt;
        this.issuedAt = issuedAt;
        this.validStartedAt = validStartedAt;
        this.validEndedAt = validEndedAt;
        this.usedAt = usedAt;
        this.status = status;
    }

    public static IssuedCouponResponse of(IssuedCoupon issuedCoupon) {
        return IssuedCouponResponse.builder()
                .id(issuedCoupon.getId())
                .couponId(issuedCoupon.getCoupon().getId())
                .name(issuedCoupon.getName())
                .discountType(issuedCoupon.getDiscountType().name())
                .discountAmt(issuedCoupon.getDiscountAmt())
                .issuedAt(issuedCoupon.getIssuedAt())
                .validStartedAt(issuedCoupon.getValidStartedAt())
                .validEndedAt(issuedCoupon.getValidEndedAt())
                .usedAt(issuedCoupon.getUsedAt())
                .status(issuedCoupon.getStatus().name())
                .build();
    }
}
