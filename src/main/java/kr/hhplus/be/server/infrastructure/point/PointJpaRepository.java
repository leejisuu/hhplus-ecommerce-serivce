package kr.hhplus.be.server.infrastructure.point;

import kr.hhplus.be.server.domain.point.entity.Point;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointJpaRepository extends JpaRepository<Point, Long> {

    default Point findByUserIdOrThrow(Long userId) {
        return findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.POINT_NOT_FOUND));
    }

    Optional<Point> findByUserId(Long userId);
}
