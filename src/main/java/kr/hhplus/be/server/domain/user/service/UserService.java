package kr.hhplus.be.server.domain.user.service;

import kr.hhplus.be.server.domain.user.dto.info.UserInfo;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserInfo.UserDto getUser(Long userId) {
        return UserInfo.UserDto.of(userRepository.findById(userId));
    }

    @Transactional
    public UserInfo.UserDto getUserWithLock(Long userId) {
        return UserInfo.UserDto.of(userRepository.findByIdWithLock(userId));
    }

}
