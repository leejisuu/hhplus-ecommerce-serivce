package kr.hhplus.be.server.domain.order.producer;

import kr.hhplus.be.server.domain.order.event.OrderEvent;

public interface OrderMessageProducer {
    void sendOrderCreatedInfo(OrderEvent.Created event);
}
