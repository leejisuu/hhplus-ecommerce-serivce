package kr.hhplus.be.server.infrastructure.user;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from user u where u.id = :userId")
    User findByIdWithLock(Long userId);
}
