package kr.hhplus.be.server.application.order.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CouponClient {
    BigDecimal reserve(Long couponId, BigDecimal totalOriginalAmt, LocalDateTime currentDateTime);

    void confirm(Long couponId);

    void cancel(Long couponId);
}
