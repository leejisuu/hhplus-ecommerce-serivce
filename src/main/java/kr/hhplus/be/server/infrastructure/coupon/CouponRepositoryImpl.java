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
    private final CouponCacheRepository couponCacheRepository;

    @Override
    public Coupon getCoupon(Long couponId) {
        return couponJpaRepository.findById(couponId).orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
    }

    @Override
    public Coupon save(Coupon coupon) {
        return couponJpaRepository.save(coupon);
    }

    @Override
    public int getCounponCount(Long couponId) {
        // 레디스 쿠폰 개수 확인
        return couponCacheRepository.getCouponCount(couponId);
    }

    @Override
    public boolean checkAlreadyIssue(Long userId, Long couponId) {
        return couponCacheRepository.checkAlreadyIssue(userId, couponId);
    }

    @Override
    public boolean addIssueRequest(Long userId, Long couponId, long currentMillis) {
        return couponCacheRepository.addIssueRequest(userId, couponId, currentMillis);
    }

    @Override
    public int decreaseCouponCount(Long couponId) {
        return couponCacheRepository.decreaseCouponCount(couponId);
    }

    @Override
    public Set<Long> getRequestUserIds(long couponId, long batchSize) {
        return couponCacheRepository.getRequestUserIds(couponId, batchSize);
    }

    @Override
    public void addIssuedCouponHistory(Long userId, long couponId) {
        couponCacheRepository.addIssuedCouponHistory(userId, couponId);
    }

    @Override
    public void setCouponCount(Long id, int maxCapacity) {
        couponCacheRepository.setCouponCount(id, maxCapacity);
    }
}
