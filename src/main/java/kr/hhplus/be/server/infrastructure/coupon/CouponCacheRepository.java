package kr.hhplus.be.server.infrastructure.coupon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.coupon.dto.CouponDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Component
public class CouponCacheRepository {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String COUPON_REMAIN_QUANTITY_KEY = "coupon-%d-remain-quantity"; // 쿠폰 잔여 개수
    private static final String COUPON_ISSUED_HISTORY_KEY = "coupon-%d-issued"; // 발급된 쿠폰 이력
    private static final String COUPON_ISSUE_REQUEST_KEY = "coupon-requests"; // 쿠폰 요청 발급

    public void setRemainCapacityToCache(Long countId, int remainCapacity) {
        String key = String.format(COUPON_REMAIN_QUANTITY_KEY, countId);
        redisTemplate.opsForValue().set(key, String.valueOf(remainCapacity));
    }

    public int getRemainCapacityFromCache(Long couponId) {
        String key = String.format(COUPON_REMAIN_QUANTITY_KEY, couponId);
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? Integer.parseInt(value.toString()) : 0;
    }

    public boolean hasCouponIssuedHistoryFromCache(Long userId, Long couponId) {
        String key = String.format(COUPON_ISSUED_HISTORY_KEY, couponId);
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, String.valueOf(userId)));
    }

    public boolean addCouponIssueRequestToCache(CouponDto couponDto) {
        String jsonCouponDto = "";
        try {
            jsonCouponDto = objectMapper.writeValueAsString(couponDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize CouponDto to JSON: " + e.getMessage(), e);
        }
        return Boolean.TRUE.equals(redisTemplate.opsForZSet().add(COUPON_ISSUE_REQUEST_KEY, jsonCouponDto, couponDto.getCurrentMillis()));
    }

    public void decreaseRemainCapacityInCache(Long couponId) {
        String key = String.format(COUPON_REMAIN_QUANTITY_KEY, couponId);
        redisTemplate.opsForValue().decrement(key);
    }

    public List<CouponDto> getCouponIssueRequestsFromCache(long batchSize) {
        Set<ZSetOperations.TypedTuple<String>> values = redisTemplate.opsForZSet().popMin(COUPON_ISSUE_REQUEST_KEY, batchSize-1);

        if(values == null) {
            return Collections.emptyList();
        }

        return values.stream()
                .map(tuple -> {
                    String request = tuple.getValue();
                    try {
                        return objectMapper.readValue(request, CouponDto.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Failed to serialize JSON to CouponDto: " + e.getMessage(), e);
                    }
                })
                .toList();
    }

    public void addCouponIssuedHistoryToCache(Long userId, Long couponId) {
        String key = String.format(COUPON_ISSUED_HISTORY_KEY, couponId);
        redisTemplate.opsForSet().add(key, String.valueOf(userId));
    }
}
