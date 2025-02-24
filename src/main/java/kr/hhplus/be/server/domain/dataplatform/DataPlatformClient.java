package kr.hhplus.be.server.domain.dataplatform;

import kr.hhplus.be.server.domain.order.event.OrderEvent;
import kr.hhplus.be.server.domain.order.event.OrderEvent.Created;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataPlatformClient {

    public void sendData(OrderEvent.Created event) {
        log.info("DataPlatformClient sendData {}", event.toString());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            log.warn("DataPlatformClient sendData exception", e);
            Thread.currentThread().interrupt();
        }
        log.info("DataPlatformClient sendData success");
    }
}
