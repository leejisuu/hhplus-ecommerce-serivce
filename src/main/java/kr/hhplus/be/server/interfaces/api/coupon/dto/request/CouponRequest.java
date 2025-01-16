package kr.hhplus.be.server.interfaces.api.coupon.dto.request;

public class CouponRequest {

    public record Issue(
            Long userId,
            Long couponId
    ) {

    }
}
