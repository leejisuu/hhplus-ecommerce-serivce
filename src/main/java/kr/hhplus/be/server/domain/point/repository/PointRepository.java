package kr.hhplus.be.server.domain.point.repository;

import kr.hhplus.be.server.domain.point.entity.Point;

public interface PointRepository {

    Point findByUserIdWithLock(Long userId);

    Point findByUserId(Long userId);
}
