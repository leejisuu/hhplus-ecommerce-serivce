package kr.hhplus.be.server.infrastructure.kafka;

import lombok.Getter;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Component
public class TestConsumer {

    private final List<String> messages = new ArrayList<>();

    @KafkaListener(topics = "test-topic", groupId = "test-group")
    public void listen(String message) {
        messages.add(message);
    }
}
