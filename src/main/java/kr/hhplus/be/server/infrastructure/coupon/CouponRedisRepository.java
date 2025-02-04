package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class CouponRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final String COUPON_REMAIN_QUANTITY_PREFIX = "coupon_quantity:"; // 쿠폰 잔여 개수
    private final String ISSUED_COUPON_HISTORY_PREFIX = "issued_coupon_history:"; // 발급된 쿠폰 이력
    private final String COUPON_ISSUE_REQUEST_PREFIX = "coupon_issue_request:"; // 쿠폰 요청 발급

    public void setCouponCount(Long countId, int maxQuantity) {
        String key = COUPON_REMAIN_QUANTITY_PREFIX + countId;
        redisTemplate.opsForValue().set(key, String.valueOf(maxQuantity));
    }

    public int getCouponCount(Long couponId) {
        String key = COUPON_REMAIN_QUANTITY_PREFIX + couponId;
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? Integer.parseInt(value) : 0;
    }

    public boolean checkAlreadyIssue(Long userId, Long couponId) {
        String key = ISSUED_COUPON_HISTORY_PREFIX + couponId;
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, String.valueOf(userId)));
    }

    public boolean addIssueRequest(Long userId, Long couponId, long currentMillis) {
        String key = COUPON_ISSUE_REQUEST_PREFIX + couponId;
        return Boolean.TRUE.equals(redisTemplate.opsForZSet().add(key, String.valueOf(userId), currentMillis));
    }

    public int decreaseCouponCount(Long couponId) {
        String key = COUPON_REMAIN_QUANTITY_PREFIX + couponId;
        long value = redisTemplate.opsForValue().decrement(key, couponId);
        return (int) value;
    }

    public Set<Long> getRequestUserIds(long couponId, long batchSize) {
        String key = COUPON_ISSUE_REQUEST_PREFIX + couponId;
        return redisTemplate.opsForZSet().popMin(key, batchSize)
                .stream()
                .map(ZSetOperations.TypedTuple::getValue) // value(String) 추출
                .map(Long::parseLong) // String → Long 변환
                .collect(Collectors.toSet());
    }

    public void addIssuedCouponHistory(Long userId, long couponId) {
        String key = ISSUED_COUPON_HISTORY_PREFIX + couponId;
        redisTemplate.opsForSet().add(key, String.valueOf(userId));
    }
}
