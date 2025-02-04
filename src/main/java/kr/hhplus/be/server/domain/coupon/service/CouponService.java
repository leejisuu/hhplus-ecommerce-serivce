package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.dto.criteria.CouponCriteria;
import kr.hhplus.be.server.domain.coupon.dto.info.IssuedCouponInfo;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.repository.IssuedCouponRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import kr.hhplus.be.server.support.distributedlock.redisson.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;

    public void create(CouponCriteria.Create criteria) {
        // 쿠폰 마스터 생성 후 레디스에 쿠폰 잔여 수량 적재
        Coupon coupon = couponRepository.save(criteria.createCoupon());
    }

    // 쿠폰 발급 요청 이력 redis sorted set에 add
    public boolean requestIssue(Long userId, Long couponId, long currentMillis) {
        // 1. 레디스로 쿠폰 잔여 개수 조회
        int couponCnt = couponRepository.getCounponCount(couponId);
        // 개수가 0 이하라면 예외 발생
        if(couponCnt <= 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_COUPON_QUANTITY);
        }

        // 2. redis 쿠폰 set에서 이미 발급 받았는지 체크
        boolean alreadyIssue = couponRepository.checkAlreadyIssue(userId, couponId);
        if(alreadyIssue) {
            throw new CustomException(ErrorCode.ALREADY_ISSUED_COUPON);
        }
        
        // 3. 발급 받지 않았다면 sorted set에 등록
        boolean addIssueRequest = couponRepository.addIssueRequest(userId, couponId, currentMillis);
        // 쿠폰 개수 하나 차감
        if(addIssueRequest) {
            couponRepository.decreaseCouponCount(couponId);
        }

        return addIssueRequest;
    }

    @Scheduled(fixedDelay = 50000)
    public void issue() {
        long batchSize = 10;
        long couponId = 100;

        // 쿠폰을 발급하기 위해 쿠폰 마스터 조회
        Coupon coupon = couponRepository.getCoupon(couponId);

        // 1. sorted set에서 해당 쿠폰에 대한 요청 정보(userId) 꺼내기
        Set<Long> userIds = couponRepository.getRequestUserIds(couponId, batchSize);

        // 2. db 쿠폰 발급 & 레디스 set에 이력 남기기
        for(Long userId : userIds) {
            issuedCouponRepository.save(coupon.issue(userId, LocalDateTime.now()));
            couponRepository.addIssuedCouponHistory(userId, couponId);
        }
    }

    @DistributedLock(key = "'Coupon:couponId:' + #couponId")
    @Transactional
    public IssuedCouponInfo.Coupon issue(Long couponId, Long userId, LocalDateTime issuedAt) {
        Coupon coupon = couponRepository.getCoupon(couponId);
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
