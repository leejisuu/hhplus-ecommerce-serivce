package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;

public interface IssuedCouponRepository {

    IssuedCoupon saveIssuedCoupon(IssuedCoupon issuedCoupon);
}
