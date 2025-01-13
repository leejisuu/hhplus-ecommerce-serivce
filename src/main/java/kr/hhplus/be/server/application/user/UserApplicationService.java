package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.application.user.dto.result.UserResult;
import kr.hhplus.be.server.domain.user.dto.info.UserInfo;
import kr.hhplus.be.server.domain.user.service.PointHistoryService;
import kr.hhplus.be.server.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserApplicationService {

    private final UserService userService;
    private final PointHistoryService pointHistoryService;

    public UserResult chargeUserPoint(Long userId, int amount) {
        // 유저 포인트 합산하여 저장
        UserInfo userInfo = userService.addUserPoint(userId, amount);

        // 충전 히스토리 내역 저장
        pointHistoryService.saveChargePointHistory(userId, amount);

        return UserResult.from(userInfo);
    }
}
