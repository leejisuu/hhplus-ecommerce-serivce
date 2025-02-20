package kr.hhplus.be.server.domain.order.event;

public interface OrderEventPublisher {

    void publish(OrderEvent.Created createEvent);
}
