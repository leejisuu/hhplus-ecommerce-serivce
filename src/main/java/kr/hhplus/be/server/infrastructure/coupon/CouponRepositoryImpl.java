package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.dto.CouponDto;
import kr.hhplus.be.server.domain.coupon.dto.command.CouponCommand;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
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
    public void setRemainCapacityToCache(Long id, int maxCapacity) {
        couponCacheRepository.setRemainCapacityToCache(id, maxCapacity);
    }

    @Override
    public int getRemainCapacityFromCache(Long couponId) {
        // 레디스 쿠폰 개수 확인
        return couponCacheRepository.getRemainCapacityFromCache(couponId);
    }

    @Override
    public boolean hasCouponIssuedHistoryFromCache(Long userId, Long couponId) {
        return couponCacheRepository.hasCouponIssuedHistoryFromCache(userId, couponId);
    }

    @Override
    public boolean addCouponIssueRequestToCache(CouponDto couponDto) {
        return couponCacheRepository.addCouponIssueRequestToCache(couponDto);
    }

    @Override
    public void decreaseRemainCapacity(Long couponId) {
        couponJpaRepository.decreaseRemainCapacity(couponId);
    }

    @Override
    public void decreaseRemainCapacityInCache(Long couponId) {
        couponCacheRepository.decreaseRemainCapacityInCache(couponId);
    }

    @Override
    public List<CouponDto> getCouponIssueRequestsFromCache(long batchSize) {
        return couponCacheRepository.getCouponIssueRequestsFromCache(batchSize);
    }

    @Override
    public void addCouponIssuedHistoryToCache(Long userId, Long couponId) {
        couponCacheRepository.addCouponIssuedHistoryToCache(userId, couponId);
    }
}
