package kr.hhplus.be.server.domain.user.dto.info;

import kr.hhplus.be.server.domain.user.entity.User;

public class UserInfo {

    public record UserDto(
            Long id,
            String name
    ) {
        public static UserInfo.UserDto of(User user) {
            return new UserInfo.UserDto(
                    user.getId(),
                    user.getName()
            );
        }
    }
}

