package kr.hhplus.be.server.infrastructure.user;

import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    default User findByUserIdOrThrow(Long userId) {
        return findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    Optional<User> findById(Long userId);
}
