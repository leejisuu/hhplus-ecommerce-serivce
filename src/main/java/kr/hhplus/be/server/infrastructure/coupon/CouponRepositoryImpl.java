package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Set;

@RequiredArgsConstructor
@Repository
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponJpaRepository;
    private final CouponRedisRepository couponRedisRepository;

    @Override
    public Coupon getCoupon(Long couponId) {
        return couponJpaRepository.findById(couponId).orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
    }

    @Override
    public Coupon save(Coupon coupon) {
        Coupon savedCoupon = couponJpaRepository.save(coupon);
        couponRedisRepository.setCouponCount(savedCoupon.getId(), savedCoupon.getMaxCapacity());
        return savedCoupon;
    }

    @Override
    public int getCounponCount(Long couponId) {
        // 레디스 쿠폰 개수 확인
        return couponRedisRepository.getCouponCount(couponId);
    }

    @Override
    public boolean checkAlreadyIssue(Long userId, Long couponId) {
        return couponRedisRepository.checkAlreadyIssue(userId, couponId);
    }

    @Override
    public boolean addIssueRequest(Long userId, Long couponId, long currentMillis) {
        return couponRedisRepository.addIssueRequest(userId, couponId, currentMillis);
    }

    @Override
    public int decreaseCouponCount(Long couponId) {
        return couponRedisRepository.decreaseCouponCount(couponId);
    }

    @Override
    public Set<Long> getRequestUserIds(long couponId, long batchSize) {
        return couponRedisRepository.getRequestUserIds(couponId, batchSize);
    }

    @Override
    public void addIssuedCouponHistory(Long userId, long couponId) {
        couponRedisRepository.addIssuedCouponHistory(userId, couponId);
    }
}
