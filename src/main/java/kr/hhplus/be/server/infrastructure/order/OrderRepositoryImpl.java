package kr.hhplus.be.server.infrastructure.order;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.order.entity.QOrder;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
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
        return jpaRepository.findById(orderId).orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Override
    public Order findByIdWithLock(Long orderId) {
        QOrder order = QOrder.order;

        return queryFactory
                .selectFrom(order)
                .where(order.id.eq(orderId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
    }
}
