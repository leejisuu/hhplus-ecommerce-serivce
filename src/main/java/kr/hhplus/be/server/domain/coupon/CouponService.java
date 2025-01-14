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

    @Transactional
    public IssuedCouponInfo issue(Long couponId, Long userId, LocalDateTime issuedAt) {
        // 비관적 락을 이용해서 쿠폰 마스터 정보를 조회한다.
        Coupon coupon = couponRepository.findByCouponIdWithLock(couponId);
        if(coupon == null) {
            throw new CustomException(ErrorCode.COUPON_NOT_FOUND);
        }

        // 유효성 체크 후 IssuedCoupon를 생성하고 저장한다
        IssuedCoupon issuedCoupon = issuedCouponRepository.save(coupon.issue(userId, issuedAt));
        return IssuedCouponInfo.of(issuedCoupon);
    }

    public Page<IssuedCouponInfo> getAvailableUserCoupons(Long userId, LocalDateTime currentTime, Pageable pageable) {
        Page<IssuedCoupon> userCouponsPage = issuedCouponRepository.getAvailableUserCoupons(userId, currentTime, pageable);

        return userCouponsPage.map(IssuedCouponInfo::of);
    }

    public IssuedCouponInfo getIssuedCouponWithLock(Long couponId, Long userId, LocalDateTime currentTime) {
        IssuedCoupon issuedCoupon = issuedCouponRepository.getIssuedCouponWithLock(couponId, userId, currentTime);
        return IssuedCouponInfo.of(issuedCoupon);
    }
}
