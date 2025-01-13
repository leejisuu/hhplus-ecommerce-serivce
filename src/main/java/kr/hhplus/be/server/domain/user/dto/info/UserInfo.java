package kr.hhplus.be.server.domain.user.dto.info;

import kr.hhplus.be.server.domain.user.entity.User;

public record UserInfo(
        Long id,
        String name,
        int point
) {
    public static UserInfo from(User user) {
        return new UserInfo(
                user.getId(),
                user.getName(),
                user.getPoint()
        );
    }
}
