package kr.hhplus.be.server.infrastructure.user;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.support.exception.CustomException;
import kr.hhplus.be.server.support.exception.ErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    default User findByUserIdOrThrow(Long userId) {
        return findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    Optional<User> findByUserId(Long userId);
}
