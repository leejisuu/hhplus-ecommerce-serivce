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
    public void setRemainCounponCount(Long id, int maxCapacity) {
        couponCacheRepository.setRemainCounponCount(id, maxCapacity);
    }

    @Override
    public int getRemainCounponCount(Long couponId) {
        // 레디스 쿠폰 개수 확인
        return couponCacheRepository.getRemainCounponCount(couponId);
    }

    @Override
    public boolean checkAlreadyIssue(Long userId, Long couponId) {
        return couponCacheRepository.checkAlreadyIssue(userId, couponId);
    }

    @Override
    public boolean addIssueRequest(CouponDto couponDto) {
        return couponCacheRepository.addIssueRequest(couponDto);
    }

    @Override
    public void decreaseCouponCountWithLock(Long couponId) {
        couponJpaRepository.decreaseCouponCountWithLock(couponId);
    }

    @Override
    public void decreaseCacheCouponCount(Long couponId) {
        couponCacheRepository.decreaseCouponCount(couponId);
    }

    @Override
    public List<CouponDto> getIssuePending(long batchSize) {
        return couponCacheRepository.getIssuePending(batchSize);
    }

    @Override
    public boolean existsCouponQuantityKey(Long couponId) {
        return couponCacheRepository.existsCouponQuantityKey(couponId);
    }
}
