package kr.hhplus.be.server.interfaces.scheuler;

import kr.hhplus.be.server.domain.coupon.service.CouponService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CouponIssueScheduler {

    private final CouponService couponService;

    public CouponIssueScheduler(CouponService couponService) {
        this.couponService = couponService;
    }

    @Scheduled(fixedDelay = 5000)
    public void issue() {
        couponService.issue();
    }
}
