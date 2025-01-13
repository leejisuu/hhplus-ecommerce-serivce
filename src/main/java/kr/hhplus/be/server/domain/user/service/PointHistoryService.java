package kr.hhplus.be.server.domain.user.service;

import kr.hhplus.be.server.domain.user.entity.PointHistory;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.domain.user.repository.PointHistoryRepository;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PointHistoryService {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public void saveChargePointHistory(Long userId, int amount) {
        User user = userRepository.findById(userId);
        pointHistoryRepository.save(PointHistory.createCharePointHistory(user, amount));
    }
}
