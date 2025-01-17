package kr.hhplus.be.server.domain.coupon.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.coupon.dto.info.IssuedCouponInfo;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.repository.IssuedCouponRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;

    @Transactional
    public IssuedCouponInfo.Coupon issue(Long couponId, Long userId, LocalDateTime issuedAt) {
        Coupon coupon = couponRepository.findByIdWithLock(couponId);
        if(coupon == null) {
            throw new CustomException(ErrorCode.COUPON_NOT_FOUND);
        }

        IssuedCoupon issuedCoupon = issuedCouponRepository.findByCouponIdAndUserId(couponId, userId);
        if (issuedCoupon != null) {
            throw new CustomException(ErrorCode.ALREADY_ISSUED_COUPON);
        }

        IssuedCoupon savedIssuedCoupon = issuedCouponRepository.save(coupon.issue(userId, issuedAt));
        return IssuedCouponInfo.Coupon.of(savedIssuedCoupon);
    }

    public Page<IssuedCouponInfo.Coupon> getPagedUserCoupons(Long userId, LocalDateTime currentTime, Pageable pageable) {
        Page<IssuedCoupon> userCouponsPage = issuedCouponRepository.getPagedUserCoupons(userId, currentTime, pageable);

        return userCouponsPage.map(IssuedCouponInfo.Coupon::of);
    }

    @Transactional
    public IssuedCouponInfo.Coupon getIssuedCouponWithLock(Long issuedCouponId, LocalDateTime currentTime) {
        IssuedCoupon issuedCoupon = issuedCouponRepository.getIssuedCouponWithLock(issuedCouponId, currentTime);
        if(issuedCoupon == null) {
            throw new CustomException(ErrorCode.ISSUED_COUPON_NOT_FOUND);
        }
        return IssuedCouponInfo.Coupon.of(issuedCoupon);
    }

    public BigDecimal useIssuedCoupon(Long issuedCouponId, BigDecimal totalOriginalAmt, LocalDateTime currentTime) {
        IssuedCoupon issuedCoupon = issuedCouponRepository.getIssuedCouponWithLock(issuedCouponId, currentTime);
        if(issuedCoupon == null) {
            throw new CustomException(ErrorCode.ISSUED_COUPON_NOT_FOUND);
        }
        return issuedCoupon.use(totalOriginalAmt, currentTime);
    }
}
