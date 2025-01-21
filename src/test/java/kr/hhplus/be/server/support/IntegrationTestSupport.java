package kr.hhplus.be.server.support;

import kr.hhplus.be.server.config.MySQLTestContainerConfig;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest
public abstract class IntegrationTestSupport implements MySQLTestContainerConfig {

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    public void cleanUpDatabase() {
        databaseCleanUp.execute();
    }
}
