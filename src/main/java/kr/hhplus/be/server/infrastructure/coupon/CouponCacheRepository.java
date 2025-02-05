package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.dto.CouponDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class CouponCacheRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String COUPON_REMAIN_QUANTITY_PREFIX = "coupon:"; // 쿠폰 잔여 개수
    private final String ISSUED_COUPON_HISTORY_PREFIX = "issued_coupon_history:"; // 발급된 쿠폰 이력
    private final String COUPON_ISSUE_REQUEST_KEY = "coupon_issue_request"; // 쿠폰 요청 발급

    public void setRemainCounponCount(Long countId, int maxQuantity) {
        String key = COUPON_REMAIN_QUANTITY_PREFIX + countId;
        redisTemplate.opsForValue().set(key, String.valueOf(maxQuantity));
    }

    public int getRemainCounponCount(Long couponId) {
        String key = COUPON_REMAIN_QUANTITY_PREFIX + couponId;
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? Integer.parseInt(value.toString()) : 0;
    }

    public boolean checkAlreadyIssue(Long userId, Long couponId) {
        String key = ISSUED_COUPON_HISTORY_PREFIX + couponId;
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, String.valueOf(userId)));
    }

    public boolean addIssueRequest(CouponDto couponDto) {
        return Boolean.TRUE.equals(redisTemplate.opsForZSet().add(COUPON_ISSUE_REQUEST_KEY, couponDto, couponDto.getCurrentMillis()));
    }

    public void decreaseCouponCount(Long couponId) {
        String key = COUPON_REMAIN_QUANTITY_PREFIX + couponId;
        redisTemplate.opsForValue().decrement(key, couponId);
    }

    public List<CouponDto> getIssuePending(long batchSize) {
        // sorted set에서 score가 작은순으로 batchSize만큼 pop
        Set<ZSetOperations.TypedTuple<Object>> values = redisTemplate.opsForZSet().popMin(COUPON_ISSUE_REQUEST_KEY, batchSize);

        if(values == null) {
            return Collections.emptyList();
        }

        return values.stream()
                .map(ZSetOperations.TypedTuple::getValue)
                .map(obj -> (CouponDto) obj)
                .toList();
    }
}
