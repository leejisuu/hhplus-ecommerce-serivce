package kr.hhplus.be.server.infrastructure.point;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.point.entity.QPoint;
import kr.hhplus.be.server.domain.point.repository.PointRepository;
import kr.hhplus.be.server.domain.point.entity.Point;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PointRepositoryImpl implements PointRepository {

    private final PointJpaRepository pointJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Point findByUserIdWithLock(Long userId) {
        QPoint point = QPoint.point1;

        return queryFactory
                .selectFrom(point)
                .where(point.userId.eq(userId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
    }

    public Point findByUserIdOrThrow(Long userId) {
        return pointJpaRepository.findByUserIdOrThrow(userId);
    }
}
