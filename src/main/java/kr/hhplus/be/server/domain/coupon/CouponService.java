package kr.hhplus.be.server.domain.coupon;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.coupon.dto.info.IssuedCouponInfo;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.enums.IssuedCouponStatus;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.repository.IssuedCouponRepository;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import kr.hhplus.be.server.interfaces.api.coupon.dto.IssuedCouponResponse;
import kr.hhplus.be.server.support.exception.CustomException;
import kr.hhplus.be.server.support.exception.ErrorCode;
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
    private final UserRepository userRepository;

    @Transactional
    public IssuedCouponInfo issueCoupon(Long couponId, Long userId, LocalDateTime issuedAt) {
        // 비관적 락을 이용해서 쿠폰 마스터 정보를 조회한다.
        Coupon coupon = couponRepository.getCouponWithLock(couponId);

        // 유저 정보를 조회한다.
        User user = userRepository.findById(userId);

        // Coupon을 통해 유효성 체크와 IssuedCoupon를 생성하고 저장한다
        IssuedCoupon issuedCoupon = issuedCouponRepository.saveIssuedCoupon(coupon.makeIssuedCoupon(user, issuedAt));
        return IssuedCouponInfo.from(issuedCoupon);
    }

    public Page<IssuedCouponInfo> getAvailableUserCoupons(Long userId, LocalDateTime currentTime, Pageable pageable) {
        Page<IssuedCoupon> userCouponsPage = issuedCouponRepository.getAvailableUserCoupons(userId, IssuedCouponStatus.UNUSED, currentTime, pageable);

        return userCouponsPage.map(IssuedCouponInfo::from);
    }

    public IssuedCouponInfo getIssuedCouponWithLock(Long couponId, Long userId) {
        IssuedCoupon issuedCoupon = issuedCouponRepository.getIssuedCouponWithLock(couponId, userId);
        return IssuedCouponInfo.from(issuedCoupon);
    }
}
