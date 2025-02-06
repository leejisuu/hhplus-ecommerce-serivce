package kr.hhplus.be.server.domain.coupon.repository;

import kr.hhplus.be.server.domain.coupon.dto.CouponDto;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;

import java.util.List;

public interface CouponRepository {

    Coupon getCoupon(Long couponId);

    Coupon save(Coupon coupon);

    void setRemainCapacityToCache(Long id, int remainCapacity);

    int getRemainCapacityFromCache(Long couponId);

    boolean hasCouponIssuedHistoryFromCache(Long userId, Long couponId);

    boolean addCouponIssueRequestToCache(CouponDto couponDto);

    void decreaseRemainCapacity(Long couponId);

    void decreaseRemainCapacityInCache(Long couponId);

    List<CouponDto> getCouponIssueRequestsFromCache(long batchSize);

    void addCouponIssuedHistoryToCache(Long userId, Long couponId);
}
