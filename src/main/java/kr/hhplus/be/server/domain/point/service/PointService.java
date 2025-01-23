package kr.hhplus.be.server.domain.point.service;

import kr.hhplus.be.server.infrastructure.redisson.RedissonLock;
import kr.hhplus.be.server.domain.point.dto.info.PointInfo;
import kr.hhplus.be.server.domain.point.entity.Point;
import kr.hhplus.be.server.domain.point.entity.PointHistory;
import kr.hhplus.be.server.domain.point.repository.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @RedissonLock(key = "'Point:userId:' + #userId")
    public PointInfo.PointDto charge(Long userId, BigDecimal amount) {
        Point point = pointRepository.findByUserId(userId);
        point.addPoint(amount);

        pointHistoryRepository.save(PointHistory.createChargePointHistory(point.getId(), amount));

        return PointInfo.PointDto.of(point);
    }

    public PointInfo.PointDto getPoint(Long userId) {
        return PointInfo.PointDto.of(pointRepository.findByUserId(userId));
    }

    public PointInfo.PointDto use(Long userId, BigDecimal amount) {
        Point point = pointRepository.findByUserId(userId);
        point.deductPoint(amount);

        pointHistoryRepository.save(PointHistory.createUsePointHistory(point.getId(), amount));

        return PointInfo.PointDto.of(point);
    }
}