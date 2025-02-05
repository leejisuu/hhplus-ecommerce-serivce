package kr.hhplus.be.server.infrastructure.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IssuedCouponCacheRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final String ISSUED_COUPON_HISTORY_PREFIX = "issued_coupon_history:"; // 발급된 쿠폰 이력

    public void uploadIssuedHistory(Long couponId, Long userId) {
        redisTemplate.opsForSet().add(ISSUED_COUPON_HISTORY_PREFIX + couponId, userId);
    }
}
