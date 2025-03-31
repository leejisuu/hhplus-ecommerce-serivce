package kr.hhplus.be.server.infrastructure.coupon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.coupon.dto.CouponDto;
import kr.hhplus.be.server.domain.coupon.repository.CouponCacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Component
public class CouponCacheRepositoryImpl implements CouponCacheRepository {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String COUPON_REMAIN_QUANTITY_KEY = "coupon-%d-remain-quantity"; // 쿠폰 잔여 개수
    private static final String COUPON_ISSUED_HISTORY_KEY = "coupon-%d-issued"; // 발급된 쿠폰 이력
    private static final String COUPON_ISSUE_REQUEST_KEY = "coupon-requests"; // 쿠폰 요청 발급

    public int getRemainCapacity(Long couponId) {
        String key = String.format(COUPON_REMAIN_QUANTITY_KEY, couponId);
        Object value = redisTemplate.opsForValue().get(key);
        return value != null ? Integer.parseInt(value.toString()) : 0;
    }

    public boolean hasCouponIssuedHistory(Long userId, Long couponId) {
        String key = String.format(COUPON_ISSUED_HISTORY_KEY, couponId);
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, String.valueOf(userId)));
    }

    public boolean addCouponIssueRequest(CouponDto couponDto) {
        String jsonCouponDto = "";
        try {
            jsonCouponDto = objectMapper.writeValueAsString(couponDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize CouponDto to JSON: " + e.getMessage(), e);
        }
        return Boolean.TRUE.equals(redisTemplate.opsForZSet().add(COUPON_ISSUE_REQUEST_KEY, jsonCouponDto, couponDto.getCurrentMillis()));
    }

    public void decreaseRemainCapacity(Long couponId) {
        String key = String.format(COUPON_REMAIN_QUANTITY_KEY, couponId);
        redisTemplate.opsForValue().decrement(key);
    }

    public List<CouponDto> getCouponIssueRequests(long batchSize) {
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

    public void addCouponIssuedHistory(Long userId, Long couponId) {
        String key = String.format(COUPON_ISSUED_HISTORY_KEY, couponId);
        redisTemplate.opsForSet().add(key, String.valueOf(userId));
    }
}
