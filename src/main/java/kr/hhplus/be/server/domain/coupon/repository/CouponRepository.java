package kr.hhplus.be.server.domain.coupon.repository;

import kr.hhplus.be.server.domain.coupon.entity.Coupon;

public interface CouponRepository {
    Coupon findByIdWithLock(Long couponId);

    Coupon findById(Long couponId);
}
