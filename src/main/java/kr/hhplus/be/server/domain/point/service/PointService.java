package kr.hhplus.be.server.domain.point.service;

import kr.hhplus.be.server.domain.point.dto.info.PointInfo;
import kr.hhplus.be.server.domain.point.entity.Point;
import kr.hhplus.be.server.domain.point.entity.PointHistory;
import kr.hhplus.be.server.domain.point.repository.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class PointService {

    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 10,
            backoff = @Backoff(delay = 500)
    )
    public PointInfo.PointDto charge(Long userId, BigDecimal amount) {
        log.info("charge() 호출됨 - userId: {}, amount: {}", userId, amount);

        Point point = pointRepository.findByUserIdWithLock(userId);
        point.addPoint(amount);

        pointHistoryRepository.save(PointHistory.createChargePointHistory(point.getId(), amount));

        return PointInfo.PointDto.of(point);
    }

    public PointInfo.PointDto getPoint(Long userId) {
        return PointInfo.PointDto.of(pointRepository.findByUserIdWithLock(userId));
    }

    @Transactional
    @Retryable(
            retryFor = ObjectOptimisticLockingFailureException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 500)
    )
    public PointInfo.PointDto use(Long userId, BigDecimal amount) {
        Point point = pointRepository.findByUserIdWithLock(userId);
        point.deductPoint(amount);

        pointHistoryRepository.save(PointHistory.createUsePointHistory(point.getId(), amount));

        return PointInfo.PointDto.of(point);
    }
}