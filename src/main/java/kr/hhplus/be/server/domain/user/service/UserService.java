package kr.hhplus.be.server.domain.user.service;

import kr.hhplus.be.server.domain.user.dto.info.UserInfo;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.domain.user.repository.PointHistoryRepository;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional
    public UserInfo addUserPoint(Long userId, BigDecimal amount) {
        User user = userRepository.findByIdWithLock(userId);
        user.addPoint(amount);

        return UserInfo.from(user);
    }

    public UserInfo getUser(Long userId) {
        return UserInfo.from(userRepository.findById(userId));
    }

    public UserInfo getUserWithLock(Long userId) {
        return UserInfo.from(userRepository.findByIdWithLock(userId));
    }

}
