package kr.hhplus.be.server.support;

import kr.hhplus.be.server.config.RedisTestContainerConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisConnectionTest implements RedisTestContainerConfig {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void testRedisIsRunningAndFunctional() {
        // Redis에 값 저장
        redisTemplate.opsForValue().set("testKey", "testValue");

        // 저장된 값 검증
        String value = redisTemplate.opsForValue().get("testKey");
        assertThat(value).isEqualTo("testValue");
    }
}
