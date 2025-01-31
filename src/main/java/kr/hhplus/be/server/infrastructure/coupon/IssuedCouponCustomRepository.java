package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface IssuedCouponCustomRepository {

    Page<IssuedCoupon> getPagedUserCoupons(Long userId, LocalDateTime currentTime, Pageable pageable);

    IssuedCoupon getIssuedCouponWithLock(Long issuedCouponId, LocalDateTime currentTime);
}
