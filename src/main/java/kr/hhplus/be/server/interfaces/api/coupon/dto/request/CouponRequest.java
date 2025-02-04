package kr.hhplus.be.server.interfaces.api.coupon.dto.request;

import kr.hhplus.be.server.domain.coupon.dto.criteria.CouponCriteria;

public class CouponRequest {

    public record Issue(
            Long userId,
            Long couponId
    ) {
        public CouponCriteria.Issue toCriteria(long currentMillis) {
            return new CouponCriteria.Issue(userId, couponId, currentMillis);
        }
    }
}
