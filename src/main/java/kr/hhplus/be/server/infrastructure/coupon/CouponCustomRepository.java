package kr.hhplus.be.server.infrastructure.coupon;

public interface CouponCustomRepository {

    void decreaseRemainCapacity(Long couponId);
}
