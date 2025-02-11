package kr.hhplus.be.server.domain.coupon.repository;

import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface IssuedCouponRepository {

    IssuedCoupon save(IssuedCoupon issuedCoupon);

    Page<IssuedCoupon> getPagedUserCoupons(Long userId, LocalDateTime currentTime, Pageable pageable);

    IssuedCoupon getIssuedCouponWithLock(Long issuedCouponId, LocalDateTime currentTime);

    IssuedCoupon findByCouponIdAndUserId(Long couponId, Long userId);

    void saveAll(List<IssuedCoupon> issuedCoupons);
}
