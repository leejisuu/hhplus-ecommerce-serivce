package kr.hhplus.be.server.domain.point.repository;

import kr.hhplus.be.server.domain.point.entity.PointHistory;

public interface PointHistoryRepository {
    PointHistory save(PointHistory pointHistory);
}
