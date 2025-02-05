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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final IssuedCouponRepository issuedCouponRepository;

    public CouponInfo.Create create(CouponCommand.Create command) {
        // 쿠폰 마스터 생성
        Coupon coupon = couponRepository.save(command.createCoupon());
        // 레디스에 쿠폰 잔여 수량 적재
        couponRepository.setRemainCounponCount(coupon.getId(), coupon.getMaxCapacity());
        return CouponInfo.Create.of(coupon);
    }

    // 쿠폰 발급 요청 이력 redis sorted set에 add
    public String issuePending(CouponCommand.Issue command) {
        // 레디스 오류를 생각해서 레디스에 쿠폰 발급 개수가 없다면 이때만 DB 조회해서 올리는게 좋을지?

        // 1. 레디스로 쿠폰 잔여 개수 조회
        int remainCounponCount = couponRepository.getRemainCounponCount(command.couponId());
        // 개수가 0 이하라면 예외 발생
        if(remainCounponCount <= 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_COUPON_QUANTITY);
        }

        // 2. redis 쿠폰 set에서 이미 발급 받았는지 체크
        boolean alreadyIssued = couponRepository.checkAlreadyIssue(command.userId(), command.couponId());
        // 이미 발급 받았다면 예외 발생
        if(alreadyIssued) {
            throw new CustomException(ErrorCode.ALREADY_ISSUED_COUPON);
        }
        
        // 3. 발급 받지 않았다면 sorted set에 등록
        boolean addIssueRequest = couponRepository.addIssueRequest(command.toCouponDto());
        // 쿠폰 개수 하나 차감
        if(addIssueRequest) {
            // 레디스에서 쿠폰 개수 차감
            couponRepository.decreaseCacheCouponCount(command.couponId());
            // DB에서 쿠폰 개수 차감
            couponRepository.decreaseCouponCountWithLock(command.couponId());
        }

        return "선착순 쿠폰 발급 요청 성공했습니다.";
    }

    // 실제 쿠폰 발급
    public void issue() {
        long batchSize = 100;

        // 쿠폰 번호와 쿠폰dto 리스트를 담을 map
        Map<Long, List<CouponDto>> map = new HashMap<>();
        List<IssuedCoupon> issuedCoupons = new ArrayList<>();

        // sorted set에서 쿠폰 발급 요청 batchSize만큼 가져오기
        List<CouponDto> requests = couponRepository.getIssuePending(batchSize);

        if (requests != null) {
            // couponId로 그룹핑
            map = requests.stream()
                    .collect(Collectors.groupingBy(CouponDto::getCouponId));

            // 쿠폰 발급
            for (Long couponId : map.keySet()) {
                Coupon couponMst = couponRepository.getCoupon(couponId);

                List<CouponDto> coupons = map.get(couponId);
                for (CouponDto couponDto : coupons) {
                    // 쿠폰 생성
                    IssuedCoupon issuedCoupon = couponMst.issue(couponDto.getUserId(), LocalDateTime.now());
                    issuedCoupons.add(issuedCoupon);

                    // 레디스에 발급 이력 올리기
                    issuedCouponRepository.uploadIssuedHistory(issuedCoupon.getCouponId(), issuedCoupon.getUserId());
                }
            }
            // 쿠폰 저장
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
