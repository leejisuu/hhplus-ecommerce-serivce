package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponJpaRepository;

    @Override
    public Coupon getCoupon(Long couponId) {
        return couponJpaRepository.findById(couponId).orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponJpaRepository.save(coupon);
    }
}
