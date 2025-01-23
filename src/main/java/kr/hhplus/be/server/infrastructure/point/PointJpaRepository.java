package kr.hhplus.be.server.infrastructure.point;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface PointJpaRepository extends JpaRepository<Point, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    Optional<Point> findByUserId(Long userId);
}
