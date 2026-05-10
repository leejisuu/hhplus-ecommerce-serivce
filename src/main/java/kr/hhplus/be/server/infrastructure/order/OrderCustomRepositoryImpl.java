package kr.hhplus.be.server.infrastructure.order;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.order.entity.QOrder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderCustomRepositoryImpl implements OrderCustomRepository {
    private final JPAQueryFactory queryFactory;
    QOrder order = QOrder.order;

    @Override
    public Order findByOrderNoWithLock(String orderNo) {
        return queryFactory
                .selectFrom(order)
                .where(order.orderNo.eq(orderNo))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
    }
}
