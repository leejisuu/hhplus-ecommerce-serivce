package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.dto.CouponDto;
import kr.hhplus.be.server.domain.coupon.dto.command.CouponCommand;
import kr.hhplus.be.server.domain.coupon.repository.CouponCacheRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import kr.hhplus.be.server.support.aop.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CouponWaitingQueueService {

    private final CouponCacheRepository couponCacheRepository;

    /*
     * 쿠폰 발급 요청 Redis에 저장
     * */
    @DistributedLock(key = "#command.couponId")
    public boolean addCouponIssueRequest(CouponCommand.AddQueue command) {
        // 캐시에 있는 쿠폰 잔여 개수 체크
        int remainCapacity = couponCacheRepository.getRemainCapacity(command.couponId());
        if(remainCapacity <= 0) throw new CustomException(ErrorCode.INSUFFICIENT_COUPON_QUANTITY);

        // 캐시에 있는 쿠폰 발급 중복 체크
        boolean hasRequest = couponCacheRepository.hasCouponIssuedHistory(command.userId(), command.couponId());
        if(hasRequest) throw new CustomException(ErrorCode.ALREADY_ISSUED_COUPON);

        // 캐시 발급 요청 sorted set에 요청 정보 등록
        boolean addRequest = couponCacheRepository.addCouponIssueRequest(command.toCouponDto());
        if(addRequest) {
            couponCacheRepository.decreaseRemainCapacity(command.couponId());
        }

        return addRequest;
    }

    public List<CouponDto> getCouponIssueRequests(long batchSize) {
        return couponCacheRepository.getCouponIssueRequests(batchSize);
    }

    public void addCouponIssuedHistory(Long userId, Long couponId) {
        couponCacheRepository.addCouponIssuedHistory(userId, couponId);
    }

}
