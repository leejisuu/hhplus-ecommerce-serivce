package kr.hhplus.be.server.infrastructure.coupon;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {

    @Modifying
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "update coupon c set c.remainCapacity = c.remainCapacity-1 where c.id = :couponId", nativeQuery = true)
    void decreaseCouponCountWithLock(Long couponId);
}
