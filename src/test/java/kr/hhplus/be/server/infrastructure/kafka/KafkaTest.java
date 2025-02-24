package kr.hhplus.be.server.infrastructure.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
class KafkaTest {
    private static final String message = "Kafka Test";

    @Autowired
    private TestProducer producer;

    @Autowired
    private TestConsumer consumer;

    @Test
    void kafkaTest() {
        producer.send("test-topic", message);
        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(Duration.ofSeconds(30))
                .untilAsserted(() -> assertThat(consumer.getConsumedMessage()).isEqualTo(message));
    }
}
