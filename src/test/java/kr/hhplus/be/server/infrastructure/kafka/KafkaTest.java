package kr.hhplus.be.server.infrastructure.kafka;
import kr.hhplus.be.server.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Testcontainers
@SpringBootTest
class KafkaTest {

    @Autowired
    private TestProducer testProducer;
    @Autowired
    private TestConsumer testConsumer;
    private final String TOPIC = "test-topic";

    @Test
    void Kafka_연동_테스트() {
        // given
        List<String> messages = new ArrayList<>();
        int cnt = 5;
        for (int i = 0; i < cnt; i++) {
            String message = "Kafka 연동 테스트 " + i;
            testProducer.send(TOPIC, message);
            messages.add(message);
        }

        // when // then
        await().pollDelay(2, TimeUnit.SECONDS)
                .atMost(10, TimeUnit.SECONDS)
                .untilAsserted(() -> assertThat(testConsumer.getMessages())
                        .hasSize(cnt));
    }
}
