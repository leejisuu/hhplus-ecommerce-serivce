package kr.hhplus.be.server.interfaces.api.coupon.dto.request;

import kr.hhplus.be.server.domain.coupon.dto.command.CouponCommand;

public class CouponRequest {

    public record Issue(
            Long userId,
            Long couponId
    ) {
        public CouponCommand.Issue toCriteria(long currentMillis) {
            return new CouponCommand.Issue(userId, couponId, currentMillis);
        }
    }
}
