package kr.hhplus.be.server.domain.point.service;

import kr.hhplus.be.server.domain.point.entity.Point;

public interface PointRepository {

    Point findByUserIdWithLock(Long userId);

    Point findByUserIdOrThrow(Long userId);
}
