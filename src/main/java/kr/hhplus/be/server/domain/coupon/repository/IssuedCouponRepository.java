package kr.hhplus.be.server.domain.coupon.repository;

import kr.hhplus.be.server.domain.coupon.enums.IssuedCouponStatus;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface IssuedCouponRepository {

    IssuedCoupon saveIssuedCoupon(IssuedCoupon issuedCoupon);

    Page<IssuedCoupon> getAvailableUserCoupons(Long userId, IssuedCouponStatus issuedCouponStatus, LocalDateTime currentTime, Pageable pageable);

    IssuedCoupon getIssuedCouponWithLock(Long couponId, Long userId);
}
