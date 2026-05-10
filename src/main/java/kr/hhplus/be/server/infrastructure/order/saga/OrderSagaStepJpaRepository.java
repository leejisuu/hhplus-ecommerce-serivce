package kr.hhplus.be.server.infrastructure.order.saga;

import kr.hhplus.be.server.domain.order.saga.OrderSagaStep;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderSagaStepJpaRepository extends JpaRepository<OrderSagaStep, Long> {
}