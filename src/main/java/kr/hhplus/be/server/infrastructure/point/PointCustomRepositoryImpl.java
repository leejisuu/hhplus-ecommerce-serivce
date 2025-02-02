package kr.hhplus.be.server.infrastructure.point;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.point.entity.Point;
import kr.hhplus.be.server.domain.point.entity.QPoint;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PointCustomRepositoryImpl implements PointCustomRepository {

    private final JPAQueryFactory queryFactory;
    QPoint point = QPoint.point1;

    @Override
    public Point findByUserIdWithLock(Long userId) {
        return queryFactory
                .selectFrom(point)
                .where(point.userId.eq(userId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
    }
}
