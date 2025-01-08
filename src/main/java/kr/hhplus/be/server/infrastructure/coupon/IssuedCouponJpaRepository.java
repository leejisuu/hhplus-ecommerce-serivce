package kr.hhplus.be.server.infrastructure.coupon;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.coupon.IssuedCouponStatus;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IssuedCouponJpaRepository extends JpaRepository<IssuedCoupon, Long> {

    List<IssuedCoupon> findAllByUserIdAndStatus(Long userId, IssuedCouponStatus issuedCouponStatus);
}
