package kr.hhplus.be.server.domain.point.service;

import kr.hhplus.be.server.domain.point.entity.PointHistory;

public interface PointHistoryRepository {
    PointHistory save(PointHistory chargePointHistory);
}
