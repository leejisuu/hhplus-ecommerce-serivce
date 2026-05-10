package kr.hhplus.be.server.infrastructure.order.saga;

import kr.hhplus.be.server.domain.order.saga.OrderSaga;
import kr.hhplus.be.server.domain.order.saga.OrderSagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class OrderSagaRepositoryImpl implements OrderSagaRepository {

    private final OrderSagaJpaRepository orderSagaJpaRepository;

    @Override
    public OrderSaga save(OrderSaga orderSaga) {
        return orderSagaJpaRepository.save(orderSaga);
    }

    @Override
    public Optional<OrderSaga> getOrderSaga(Long sagaId) {
        return orderSagaJpaRepository.findById(sagaId);
    }

    @Override
    public Optional<OrderSaga> findByOrderNo(String orderNo) {
        return orderSagaJpaRepository.findByOrderNo(orderNo);
    }
}