package kr.hhplus.be.server.infrastructure.order.saga;

import kr.hhplus.be.server.domain.order.saga.OrderSagaStep;
import kr.hhplus.be.server.domain.order.saga.OrderSagaStepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OrderSagaStepRepositoryImpl implements OrderSagaStepRepository {

    private final OrderSagaStepJpaRepository orderSagaStepJpaRepository;

    @Override
    public OrderSagaStep save(OrderSagaStep orderSagaStep) {
        return orderSagaStepJpaRepository.save(orderSagaStep);
    }
}