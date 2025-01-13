package kr.hhplus.be.server.infrastructure.user;

import kr.hhplus.be.server.domain.user.repository.UserRepository;
import kr.hhplus.be.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User findByIdWithLock(Long userId) {
        return userJpaRepository.findByIdWithLock(userId);
    }

    @Override
    public User findById(Long userId) {
        return userJpaRepository.findById(userId).orElseThrow();
    }
}
