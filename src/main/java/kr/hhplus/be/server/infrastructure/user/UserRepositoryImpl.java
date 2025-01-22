package kr.hhplus.be.server.infrastructure.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import kr.hhplus.be.server.domain.user.entity.QUser;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import kr.hhplus.be.server.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public User findByIdWithLock(Long userId) {
        QUser user = QUser.user;

        return queryFactory
                .selectFrom(user)
                .where(user.id.eq(userId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
    }

    @Override
    public User findById(Long userId) {
        return userJpaRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
