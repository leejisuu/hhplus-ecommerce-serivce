package kr.hhplus.be.server.config;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
public interface RedisTestContainerConfig {

    @ServiceConnection
    RedisContainer REDIS_CONTAINER = new RedisContainer(DockerImageName.parse("redis"));

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        if (!REDIS_CONTAINER.isRunning()) {
            REDIS_CONTAINER.start(); // 컨테이너 시작
        }

        // Spring Redis 연결 정보 설정
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port", () -> REDIS_CONTAINER.getFirstMappedPort());
    }
}