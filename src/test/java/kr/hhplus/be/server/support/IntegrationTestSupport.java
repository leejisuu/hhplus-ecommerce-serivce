package kr.hhplus.be.server.support;

import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.junit.jupiter.Testcontainers;

@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Testcontainers
@SpringBootTest
public abstract class IntegrationTestSupport {

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @AfterEach
    public void cleanUpDatabase() {
        databaseCleanUp.execute();
    }

    @AfterEach
    public void cleanUpRedis() {
        redisTemplate.delete(redisTemplate.keys("*"));
    }


}
