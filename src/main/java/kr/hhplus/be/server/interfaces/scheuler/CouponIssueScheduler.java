package kr.hhplus.be.server.interfaces.scheuler;

import kr.hhplus.be.server.domain.coupon.dto.CouponDto;
import kr.hhplus.be.server.domain.coupon.dto.command.CouponCommand;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import kr.hhplus.be.server.domain.coupon.service.CouponWaitingQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CouponIssueScheduler {

    private final CouponService couponService;
    private final CouponWaitingQueueService couponWaitingQueueService;

    @Scheduled(cron = "0 */2 * * * *")
    public void issue() {
        long batchSize = 100;

        Map<Long, List<CouponDto>> couponMap;

        // Redis에서 쿠폰 발급 요청 pop
        List<CouponDto> issueRequests = couponWaitingQueueService.getCouponIssueRequests(batchSize);

        if (issueRequests != null) {
            couponMap = issueRequests.stream()
                    .collect(Collectors.groupingBy(CouponDto::getCouponId));

            for (Long couponId : couponMap.keySet()) {
                List<CouponDto> couponDtos = couponMap.get(couponId);
                for (CouponDto couponDto : couponDtos) {
                    couponService.issue(CouponCommand.Issue.of(couponDto));
                    couponWaitingQueueService.addCouponIssuedHistory(couponDto.getUserId(), couponDto.getCouponId());
                }
            }
        }
    }
}
