package kr.hhplus.be.server.infrastructure.user;

import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User getUserWithLock(Long userId) {
        return userJpaRepository.findByIdWithLock(userId);
    }

    @Override
    public User getUser(Long userId) {
        return userJpaRepository.findById(userId).orElseThrow();
    }
}
