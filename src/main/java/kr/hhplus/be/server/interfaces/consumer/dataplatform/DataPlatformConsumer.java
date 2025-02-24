package kr.hhplus.be.server.interfaces.consumer.dataplatform;

import kr.hhplus.be.server.domain.dataplatform.DataPlatformClient;
import kr.hhplus.be.server.domain.order.event.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataPlatformConsumer {

    private final DataPlatformClient dataPlatformClient;
    private static final String GROUP_ID = "data_platform";

    @KafkaListener(topics = "${order-api.order-created.topic-name}", groupId = GROUP_ID)
    public void consume(OrderEvent.Created event) {
        dataPlatformClient.sendData(event);
    }
}
