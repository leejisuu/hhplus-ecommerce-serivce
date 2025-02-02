package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.repository.IssuedCouponRepository;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Repository
public class IssuedCouponRepositoryImpl implements IssuedCouponRepository {

    private final IssuedCouponJpaRepository issuedCouponJpaRepository;

    @Override
    public IssuedCoupon save(IssuedCoupon issuedCoupon) {
        return issuedCouponJpaRepository.save(issuedCoupon);
    }

    @Override
    public Page<IssuedCoupon> getPagedUserCoupons(Long userId, LocalDateTime currentTime, Pageable pageable) {
        return issuedCouponJpaRepository.getPagedUserCoupons(userId, currentTime, pageable);
    }

    @Override
    public IssuedCoupon getIssuedCouponWithLock(Long issuedCouponId, LocalDateTime currentTime) {
        return issuedCouponJpaRepository.getIssuedCouponWithLock(issuedCouponId, currentTime);
    }

    @Override
    public IssuedCoupon findByCouponIdAndUserId(Long couponId, Long userId) {
        return issuedCouponJpaRepository.findByCouponIdAndUserId(couponId, userId);
    }
}
