package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.dto.CouponDto;
import kr.hhplus.be.server.domain.coupon.dto.command.CouponCommand;
import kr.hhplus.be.server.domain.coupon.dto.info.CouponInfo;
import kr.hhplus.be.server.domain.coupon.dto.info.IssuedCouponInfo;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.repository.IssuedCouponRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import kr.hhplus.be.server.support.aop.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;
    private final StringRedisTemplate redisTemplate;

    public CouponInfo.Create create(CouponCommand.Create command) {
        Coupon coupon = couponRepository.save(command.createCoupon());
        redisTemplate.opsForValue().set(String.format("coupon-%d-remain-quantity", coupon.getId()), String.valueOf(coupon.getRemainCapacity()));
        return CouponInfo.Create.of(coupon);
    }

    /*
    * 쿠폰 발급
    * */
    @DistributedLock(key = "'Coupon:couponId:' + #couponId")
    @Transactional
    public IssuedCouponInfo.Coupon issue(CouponCommand.Issue command) {
        Long couponId = command.couponId();
        Long userId = command.userId();

        Coupon coupon = couponRepository.getCoupon(couponId);
        if(coupon == null) {
            throw new CustomException(ErrorCode.COUPON_NOT_FOUND);
        }

        IssuedCoupon issuedCoupon = issuedCouponRepository.findByCouponIdAndUserId(couponId, userId);
        if (issuedCoupon != null) {
            throw new CustomException(ErrorCode.ALREADY_ISSUED_COUPON);
        }

        IssuedCoupon savedIssuedCoupon = issuedCouponRepository.save(coupon.issue(userId, command.currentDateTime()));

        return IssuedCouponInfo.Coupon.of(savedIssuedCoupon);
    }

    public Page<IssuedCouponInfo.Coupon> getPagedUserCoupons(Long userId, LocalDateTime currentTime, Pageable pageable) {
        Page<IssuedCoupon> userCouponsPage = issuedCouponRepository.getPagedUserCoupons(userId, currentTime, pageable);

        return userCouponsPage.map(IssuedCouponInfo.Coupon::of);
    }

    @Transactional
    public BigDecimal useIssuedCoupon(Long issuedCouponId, BigDecimal totalOriginalAmt, LocalDateTime currentTime) {
        if(issuedCouponId == null) {
            return BigDecimal.ZERO;
        }

        IssuedCoupon issuedCoupon = issuedCouponRepository.getIssuedCouponWithLock(issuedCouponId, currentTime);
        if(issuedCoupon == null) {
            throw new CustomException(ErrorCode.ISSUED_COUPON_NOT_FOUND);
        }
        return issuedCoupon.use(totalOriginalAmt, currentTime);
    }
}
