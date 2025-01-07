package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.interfaces.api.user.dto.UserPointChargeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User chargeUserPoint(UserPointChargeRequest userPointChargeRequest) {
        User user = userRepository.getUser(userPointChargeRequest.getUserId());

        user.addPoint(userPointChargeRequest.getChargeAmt());

        return user;
    }

    public User getUser(Long userId) {
        return userRepository.getUser(userId);
    }
}
