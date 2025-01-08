package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.user.entity.PointHistory;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.interfaces.api.user.dto.UserPointChargeRequest;
import kr.hhplus.be.server.interfaces.api.user.dto.UserPointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;

    public UserPointResponse getUserPoint(Long userId) {
        return UserPointResponse.from(userRepository.getUser(userId));
    }

    @Transactional
    public UserPointResponse chargeUserPoint(UserPointChargeRequest userPointChargeRequest) {
        Long userId = userPointChargeRequest.userId();
        int chargeAmt = userPointChargeRequest.chargeAmt();

        // 유저 정보 조회
        User user = userRepository.getUserWithLock(userId);
        // 유저가 보유한 포인트에 충전 요청 금액 합산
        user.addPoint(chargeAmt);

        // 포인트 히스토리 생성 및 연관 관계 설정
        PointHistory pointHistory = PointHistory.createChargePointHistory(user);
        user.addPointHistories(pointHistory);

        // PointHistory 저장
        pointHistoryRepository.save(pointHistory);

        return UserPointResponse.from(user);
    }

    public User getUser(Long userId) {
        return userRepository.getUser(userId);
    }

}
