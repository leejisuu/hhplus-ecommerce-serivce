package kr.hhplus.be.server.infrastructure.user;

import kr.hhplus.be.server.domain.user.PointHistoryRepository;
import kr.hhplus.be.server.domain.user.entity.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

private final PointHistoryJpaRepository pointHistoryJpaRepository;

    @Override
    public PointHistory save(PointHistory chargePointHistory) {
        return pointHistoryJpaRepository.save(chargePointHistory);
    }
}
