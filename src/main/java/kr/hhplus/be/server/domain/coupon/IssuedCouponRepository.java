package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;

import java.util.List;

public interface IssuedCouponRepository {

    IssuedCoupon saveIssuedCoupon(IssuedCoupon issuedCoupon);

    List<IssuedCoupon> getUserCouponsByUserIdAndStatus(Long userId);
}
