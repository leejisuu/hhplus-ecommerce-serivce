package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.IssuedCouponRepository;
import kr.hhplus.be.server.domain.coupon.IssuedCouponStatus;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class IssuedCouponRepositoryImpl implements IssuedCouponRepository {

    private final IssuedCouponJpaRepository issuedCouponJpaRepository;

    @Override
    public IssuedCoupon saveIssuedCoupon(IssuedCoupon issuedCoupon) {
        return issuedCouponJpaRepository.save(issuedCoupon);
    }

    @Override
    public List<IssuedCoupon> getUserCouponsByUserIdAndStatus(Long userId) {
        return issuedCouponJpaRepository.findAllByUserIdAndStatus(userId, IssuedCouponStatus.UNUSED);
    }
}
