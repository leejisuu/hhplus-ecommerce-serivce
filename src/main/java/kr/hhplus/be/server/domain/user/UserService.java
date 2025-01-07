package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.interfaces.api.user.dto.UserPointChargeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User chargeUserPoint(UserPointChargeRequest userPointChargeRequest) {
        User user = userRepository.getUser(userPointChargeRequest.getUserId());

        user.addPoint(userPointChargeRequest.getChargeAmt());

        return user;
    }

}
