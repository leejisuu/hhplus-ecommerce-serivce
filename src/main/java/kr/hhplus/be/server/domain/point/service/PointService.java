package kr.hhplus.be.server.domain.point.service;

import kr.hhplus.be.server.domain.point.dto.info.PointInfo;
import kr.hhplus.be.server.domain.point.entity.Point;
import kr.hhplus.be.server.domain.point.entity.PointHistory;
import kr.hhplus.be.server.domain.user.dto.info.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public PointInfo charge(Long userId, BigDecimal amount) {
        Point point = pointRepository.findByUserIdWithLock(userId);
        point.addPoint(amount);

        pointHistoryRepository.save(PointHistory.createChargePointHistory(point.getId(), amount));

        return PointInfo.of(point);
    }


    public PointInfo getPoint(Long userId) {
        return PointInfo.of(pointRepository.findByUserIdOrThrow(userId));
    }
}