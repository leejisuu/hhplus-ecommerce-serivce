package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssuedCouponJpaRepository extends JpaRepository<IssuedCoupon, Long>, IssuedCouponCustomRepository {

    IssuedCoupon findByCouponIdAndUserId(Long couponId, Long userid);

    List<IssuedCoupon> findAllByCouponId(Long couponId);

    List<IssuedCoupon> findAllByCouponIdAndUserId(Long couponId, Long userId);
}
