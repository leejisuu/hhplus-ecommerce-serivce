package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Order findById(Long orderId) {
        return orderJpaRepository.findById(orderId).orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Override
    public Order findByIdWithLock(Long orderId) {
        return orderJpaRepository.findByIdWithLock(orderId);
    }
}
