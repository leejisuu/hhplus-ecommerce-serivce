package kr.hhplus.be.server.domain.order.event;

import java.math.BigDecimal;

public class OrderCreateEvent {
    private final Long id;
    private final BigDecimal totalOriginalAmt;

    private OrderCreateEvent(Long id, BigDecimal totalOriginalAmt) {
        this.id = id;
        this.totalOriginalAmt = totalOriginalAmt;
    }

    public static OrderCreateEvent create(Long id, BigDecimal totalOriginalAmt) {
        return new OrderCreateEvent(id, totalOriginalAmt);
    }

    @Override
    public String toString() {
        return "OrderCreateEvent{" +
                "id=" + id +
                ", totalOriginalAmt=" + totalOriginalAmt +
                '}';
    }
}
