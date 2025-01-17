package kr.hhplus.be.server.domain.dataplatform;

import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;
import org.springframework.stereotype.Component;

@Component
public class DataPlatformClient {

    public boolean sendData(OrderInfo.OrderDto order) {
        return true;
    }
}
