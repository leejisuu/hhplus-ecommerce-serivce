package kr.hhplus.be.server.interfaces.api.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UserCouponsResponse {
    private Long id;
    private List<UserCouponResponse> userCoupons;

    @Builder
    public UserCouponsResponse(Long id, List<UserCouponResponse> userCoupons) {
        this.id = id;
        this.userCoupons = userCoupons;
    }

}
