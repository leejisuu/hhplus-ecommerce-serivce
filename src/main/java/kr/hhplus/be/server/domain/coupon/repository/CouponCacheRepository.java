package kr.hhplus.be.server.domain.coupon.repository;

import kr.hhplus.be.server.domain.coupon.dto.CouponDto;

import java.util.List;

public interface CouponCacheRepository {
    int getRemainCapacity(Long couponId);

    boolean hasCouponIssuedHistory(Long userId, Long couponId);

    boolean addCouponIssueRequest(CouponDto couponDto);

    void decreaseRemainCapacity(Long aLong);

    List<CouponDto> getCouponIssueRequests(long batchSize);

    void addCouponIssuedHistory(Long userId, Long couponId);
}
