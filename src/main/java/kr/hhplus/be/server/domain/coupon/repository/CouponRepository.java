package kr.hhplus.be.server.domain.coupon.repository;

import kr.hhplus.be.server.domain.coupon.entity.Coupon;

import java.util.Set;

public interface CouponRepository {

    Coupon getCoupon(Long couponId);

    Coupon save(Coupon coupon);

    int getCounponCount(Long couponId);

    boolean checkAlreadyIssue(Long userId, Long couponId);

    boolean addIssueRequest(Long userId, Long couponId, long currentMillis);

    int decreaseCouponCount(Long couponId);

    Set<Long> getRequestUserIds(long couponId, long batchSize);

    void addIssuedCouponHistory(Long userId, long couponId);

    void setCouponCount(Long id, int maxCapacity);
}
