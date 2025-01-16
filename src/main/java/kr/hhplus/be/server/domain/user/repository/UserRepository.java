package kr.hhplus.be.server.domain.user.repository;

import kr.hhplus.be.server.domain.user.entity.User;

public interface UserRepository {
    User findByUserIdOrThrow(Long userId);

    User findByUserIdWithLock(Long userId);
}
