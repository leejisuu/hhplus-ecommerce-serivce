package kr.hhplus.be.server.domain.order.event;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderEvent {

    public record Created(
            Long orderId,
            BigDecimal totalOriginalAmt,
            String messageId
    ) {
        public static OrderEvent.Created create(Long orderId, BigDecimal totalOriginalAmt) {
            return new OrderEvent.Created(orderId, totalOriginalAmt, UUID.randomUUID().toString());
        }

        @Override
        public String toString() {
            return "OrderEvent.Created{" +
                    "orderId=" + orderId +
                    ", totalOriginalAmt=" + totalOriginalAmt +
                    '}';
        }
    }




}
