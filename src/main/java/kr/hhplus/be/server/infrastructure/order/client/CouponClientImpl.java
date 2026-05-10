package kr.hhplus.be.server.infrastructure.order.client;

import kr.hhplus.be.server.application.order.client.CouponClient;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CouponClientImpl implements CouponClient {

    private final CouponService couponService;

    @Override
    public void confirm(Long couponId) {
        couponService.confirm(couponId);
    }

    @Override
    public BigDecimal reserve(Long couponId, BigDecimal totalOriginalAmt, LocalDateTime currentDateTime) {
        return couponService.reserve(couponId, totalOriginalAmt, currentDateTime);
    }

    @Override
    public void cancel(Long couponId) {
        couponService.cancel(couponId);
    }
}
