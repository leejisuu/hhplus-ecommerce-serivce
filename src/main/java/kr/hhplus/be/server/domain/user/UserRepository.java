package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.user.entity.User;

public interface UserRepository {
    User getUser(Long userId);

    enum PointType {
    }
}
