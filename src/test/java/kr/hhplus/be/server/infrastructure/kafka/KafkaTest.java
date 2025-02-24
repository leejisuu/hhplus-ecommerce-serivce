package kr.hhplus.be.server.infrastructure.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class KafkaTest {
    private static final String TOPIC = "test";
    private static final AtomicInteger counter = new AtomicInteger(0);

    @Autowired
    private KafkaTemplate<String, Object> producer;

    @KafkaListener(topics = TOPIC, groupId = "test_group")
    public void listen(String message) {
        counter.incrementAndGet();
    }

    @Test
    void kafkaTest() {
        producer.send(TOPIC, "Kafka Test");
        await()
                .pollInterval(Duration.ofMillis(300))
                .atMost(Duration.ofSeconds(2))
                .untilAsserted(() -> assertThat(counter.get()).isEqualTo(1L));
    }
}
