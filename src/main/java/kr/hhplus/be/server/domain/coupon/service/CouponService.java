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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public CouponInfo.Create create(CouponCommand.Create command) {
        Coupon coupon = couponRepository.save(command.createCoupon());
        couponRepository.setRemainCapacityToCache(coupon.getId(), coupon.getRemainCapacity());
        return CouponInfo.Create.of(coupon);
    }

    /*
    * 쿠폰 발급 요청 Redis에 저장
    * */
    public boolean addCouponIssueRequest(CouponCommand.Issue command) {
        // 캐시에 있는 쿠폰 잔여 개수 체크
        int remainCapacity = couponRepository.getRemainCapacityFromCache(command.couponId());
        if(remainCapacity <= 0) throw new CustomException(ErrorCode.INSUFFICIENT_COUPON_QUANTITY);

        // 캐시에 있는 쿠폰 발급 중복 체크
        boolean hasRequest = couponRepository.hasCouponIssuedHistoryFromCache(command.userId(), command.couponId());
        if(hasRequest) throw new CustomException(ErrorCode.ALREADY_ISSUED_COUPON);

        // 캐시 발급 요청 sorted set에 요청 정보 등록
        boolean addRequest = couponRepository.addCouponIssueRequestToCache(command.toCouponDto());
        if(addRequest) {
            couponRepository.decreaseRemainCapacityInCache(command.couponId());
        }

        return addRequest;
    }

    /*
    * 쿠폰 발급
    * */
    @Transactional
    public void issue() {
        long batchSize = 100;

        Map<Long, List<CouponDto>> couponMap;
        List<IssuedCoupon> issuedCoupons = new ArrayList<>();

        // Redis에서 쿠폰 발급 요청 pop
        List<CouponDto> issueRequests = couponRepository.getCouponIssueRequestsFromCache(batchSize);

        if (issueRequests != null) {
            couponMap = issueRequests.stream()
                    .collect(Collectors.groupingBy(CouponDto::getCouponId));

            for (Long couponId : couponMap.keySet()) {
                Coupon couponMst = couponRepository.getCoupon(couponId);

                List<CouponDto> couponDtos = couponMap.get(couponId);
                for (CouponDto couponDto : couponDtos) {
                    // 쿠폰 발급 처리
                    IssuedCoupon issuedCoupon = couponMst.issue(couponDto.getUserId(), LocalDateTime.now());
                    issuedCoupons.add(issuedCoupon);

                    // 쿠폰 잔여 개수 차감
                    couponMst.decreaseRemainCapacity();
                    couponRepository.save(couponMst);
                    // Redis에 쿠폰 발급 이력 add
                    couponRepository.addCouponIssuedHistoryToCache(issuedCoupon.getUserId(), issuedCoupon.getCouponId());
                }
            }
            // 쿠폰 bulk 저장
            issuedCouponRepository.saveAll(issuedCoupons);
        }
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
