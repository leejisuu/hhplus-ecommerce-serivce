package kr.hhplus.be.server.domain.dataplatform;

import kr.hhplus.be.server.domain.order.event.OrderCreateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataPlatformClient {

    public void sendData(OrderCreateEvent orderCreateEvent) {
        log.info("DataPlatformClient sendData {}", orderCreateEvent.toString());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            log.warn("DataPlatformClient sendData exception", e);
            Thread.currentThread().interrupt();
        }
        log.info("DataPlatformClient sendData success");
    }
}
