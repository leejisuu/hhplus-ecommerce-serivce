package kr.hhplus.be.server.interfaces.api.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserCouponResponse {

    private Long id;
    private Long couponId;
    private String couponName;
    private String discountType;
    private int discountAmt;
    private LocalDate validStartAt;
    private LocalDate validEndAt;
    private String status;

    @Builder
    public UserCouponResponse(Long id, Long couponId, String couponName, String discountType, int discountAmt, LocalDate validStartAt, LocalDate validEndAt, String status) {
        this.id = id;
        this.couponId = couponId;
        this.couponName = couponName;
        this.discountType = discountType;
        this.discountAmt = discountAmt;
        this.validStartAt = validStartAt;
        this.validEndAt = validEndAt;
        this.status = status;
    }
}
