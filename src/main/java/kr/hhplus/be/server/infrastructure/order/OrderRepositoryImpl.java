package kr.hhplus.be.server.infrastructure.order;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.domain.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Order save(Order order) {
        return jpaRepository.save(order);
    }

    @Override
    public Order findById(Long orderId) {
        return jpaRepository.findById(orderId).orElse(null);
    }
}
