package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;

    public IssuedCoupon issueCoupon(Long couponId, User user, LocalDateTime issuedAt) {
        Coupon coupon = couponRepository.getCouponWithLock(couponId);
        return issuedCouponRepository.saveIssuedCoupon(coupon.makeIssuedCoupon(user, issuedAt));
    }
}
