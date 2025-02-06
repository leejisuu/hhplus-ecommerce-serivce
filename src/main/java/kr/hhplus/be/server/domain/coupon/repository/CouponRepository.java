package kr.hhplus.be.server.domain.coupon.repository;

import kr.hhplus.be.server.domain.coupon.dto.CouponDto;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;

import java.util.List;

public interface CouponRepository {

    Coupon getCoupon(Long couponId);

    Coupon save(Coupon coupon);

    void setRemainCounponCount(Long id, int maxCapacity);

    int getRemainCounponCount(Long couponId);

    boolean checkAlreadyIssue(Long userId, Long couponId);

    boolean addIssueRequest(CouponDto couponDto);

    void decreaseCouponCountWithLock(Long couponId);

    void decreaseCacheCouponCount(Long couponId);

    List<CouponDto> getIssuePending(long batchSize);

    boolean existsCouponQuantityKey(Long aLong);
}
