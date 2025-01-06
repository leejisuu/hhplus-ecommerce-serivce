package kr.hhplus.be.server.interfaces.api.coupon.dto;


import lombok.Builder;

import java.time.LocalDateTime;

public class CouponResponse {
    private Long id;
    private Long couponId;
    private String name;
    private String discountType;
    private int discountAmt;
    private LocalDateTime issuedAt;
    private LocalDateTime validStartAt;
    private LocalDateTime validEndAt;
    private LocalDateTime usedAt;
    private String status;

    @Builder
    public CouponResponse(Long id, Long couponId, String name, String discountType, int discountAmt, LocalDateTime issuedAt, LocalDateTime validStartAt, LocalDateTime validEndAt, LocalDateTime usedAt, String status) {
        this.id = id;
        this.couponId = couponId;
        this.name = name;
        this.discountType = discountType;
        this.discountAmt = discountAmt;
        this.issuedAt = issuedAt;
        this.validStartAt = validStartAt;
        this.validEndAt = validEndAt;
        this.usedAt = usedAt;
        this.status = status;
    }
}
