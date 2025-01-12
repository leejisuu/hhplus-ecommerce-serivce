package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.interfaces.api.coupon.dto.IssuedCouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class CouponFacade {

    private final CouponService couponService;
    private final UserService userService;

    @Transactional
    public IssuedCouponResponse issueCoupon(Long couponId, Long userId, LocalDateTime issuedAt) {
        // 유저를 조회한다.
        User user = userService.getUser(userId);
        // CouponService를 통해 coupon을 발급한다.
        IssuedCoupon issuedCoupon = couponService.issueCoupon(couponId, user, issuedAt);

        return IssuedCouponResponse.of(issuedCoupon);
    }
}
