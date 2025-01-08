package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.user.entity.PointHistory;

public interface PointHistoryRepository {
    PointHistory save(PointHistory chargePointHistory);
}
