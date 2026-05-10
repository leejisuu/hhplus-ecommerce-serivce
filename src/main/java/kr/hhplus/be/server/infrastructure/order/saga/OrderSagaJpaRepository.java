package kr.hhplus.be.server.infrastructure.order.saga;

import kr.hhplus.be.server.domain.order.saga.OrderSaga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderSagaJpaRepository extends JpaRepository<OrderSaga, Long> {
    Optional<OrderSaga> findByOrderNo(String orderNo);
}