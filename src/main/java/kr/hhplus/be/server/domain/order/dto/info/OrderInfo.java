package kr.hhplus.be.server.domain.order.dto.info;

import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.order.event.OrderCreateEvent;

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

        public OrderCreateEvent toCreateEvent() {
            return OrderCreateEvent.create(id, totalOriginalAmt);
        }
    }
}