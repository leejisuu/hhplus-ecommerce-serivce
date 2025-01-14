package kr.hhplus.be.server.infrastructure.point;

import kr.hhplus.be.server.domain.point.service.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.entity.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

private final PointHistoryJpaRepository pointHistoryJpaRepository;

    @Override
    public PointHistory save(PointHistory pointHistory) {
        return pointHistoryJpaRepository.save(pointHistory);
    }
}
