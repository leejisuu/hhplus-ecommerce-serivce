package kr.hhplus.be.server.domain.dataplatform;

import kr.hhplus.be.server.domain.order.event.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/*
* 주문 정보를 Kafak로 전달하면 주문 도메인에서의 책임은 끝이므로 해당 소스 미사용
* */
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
