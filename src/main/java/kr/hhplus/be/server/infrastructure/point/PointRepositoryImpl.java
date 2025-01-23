package kr.hhplus.be.server.infrastructure.point;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.point.entity.QPoint;
import kr.hhplus.be.server.domain.point.repository.PointRepository;
import kr.hhplus.be.server.domain.point.entity.Point;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PointRepositoryImpl implements PointRepository {

    private final PointJpaRepository pointJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Point findByUserIdWithLock(Long userId) {
        return pointJpaRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.POINT_NOT_FOUND));
    }

    public Point findByUserId(Long userId) {
        return pointJpaRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.POINT_NOT_FOUND));
    }
}
