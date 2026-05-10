package kr.hhplus.be.server.domain.order.saga;

public interface OrderSagaStepRepository {
    OrderSagaStep save(OrderSagaStep orderSagaStep);
}