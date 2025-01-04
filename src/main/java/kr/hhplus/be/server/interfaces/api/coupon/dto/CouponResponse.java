package kr.hhplus.be.server.interfaces.api.coupon.dto;


import lombok.Builder;

import java.time.LocalDate;

public class CouponResponse {
    private Long id;
    private String name;
    private String discountType;
    private int discountAmt;
    private LocalDate validStartAt;
    private LocalDate validEndAt;
    private String status;

    @Builder
    public CouponResponse(Long id, String name, String discountType, int discountAmt, LocalDate validStartAt, LocalDate validEndAt, String status) {
        this.id = id;
        this.name = name;
        this.discountType = discountType;
        this.discountAmt = discountAmt;
        this.validStartAt = validStartAt;
        this.validEndAt = validEndAt;
        this.status = status;
    }

}
