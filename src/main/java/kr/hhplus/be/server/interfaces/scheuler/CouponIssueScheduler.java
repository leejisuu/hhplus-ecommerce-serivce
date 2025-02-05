package kr.hhplus.be.server.interfaces.scheuler;

import kr.hhplus.be.server.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CouponIssueScheduler {

    private final CouponService couponService;

    @Scheduled(fixedDelay = 5000)
    public void issue() {
        couponService.issue();
    }
}
