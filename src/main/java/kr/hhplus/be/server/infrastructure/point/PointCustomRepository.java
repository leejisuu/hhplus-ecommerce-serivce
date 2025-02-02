package kr.hhplus.be.server.infrastructure.point;

import kr.hhplus.be.server.domain.point.entity.Point;

public interface PointCustomRepository {

    Point findByUserIdWithLock(Long userId);
}
