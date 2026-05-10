package kr.hhplus.be.server.domain.order.saga;

import java.util.Optional;

public interface OrderSagaRepository {
    OrderSaga save(OrderSaga orderSaga);
    Optional<OrderSaga> getOrderSaga(Long sagaId);
    Optional<OrderSaga> findByOrderNo(String orderNo);
}