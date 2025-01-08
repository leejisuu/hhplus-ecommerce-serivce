package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.enums.IssuedCouponStatus;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.repository.IssuedCouponRepository;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.interfaces.api.coupon.dto.IssuedCouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;

    public IssuedCoupon issueCoupon(Long couponId, User user, LocalDateTime issuedAt) {
        // 비관적 락을 이용해서 쿠폰 마스터 정보를 조회한다.
        Coupon coupon = couponRepository.getCouponWithLock(couponId);
        // coupon을 통해 유효성 체크와 IssuedCoupon를 생성하여 반환한다.
        IssuedCoupon issuedCoupon = coupon.makeIssuedCoupon(user, issuedAt);
        return issuedCouponRepository.saveIssuedCoupon(issuedCoupon);
    }

    public Page<IssuedCouponResponse> getAvailableUserCoupons(Long userId, LocalDateTime currentTime, Pageable pageable) {
        Page<IssuedCoupon> userCouponsPage = issuedCouponRepository.getAvailableUserCoupons(userId, IssuedCouponStatus.UNUSED, currentTime, pageable);

        return userCouponsPage.map(IssuedCouponResponse::of);
    }
}
