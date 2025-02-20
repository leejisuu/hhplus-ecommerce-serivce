package kr.hhplus.be.server.domain.order.dto.info;

import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.order.event.OrderEvent;

import java.math.BigDecimal;

public class OrderInfo {

    public record OrderDto(
            Long id,
            BigDecimal totalOriginalAmt
    ) {
        public static OrderInfo.OrderDto of(Order order) {

            return new OrderInfo.OrderDto(
                    order.getId(),
                    order.getTotalOriginalAmt()
            );
        }

        public OrderEvent.Created toCreatedEvent() {
            return OrderEvent.Created.create(id, totalOriginalAmt);
        }
    }
}