package kr.hhplus.be.server.interfaces.api.user.dto;

import kr.hhplus.be.server.application.user.dto.result.UserResult;
import kr.hhplus.be.server.domain.user.dto.info.UserInfo;

public record UserResponse(
        Long id,
        int point
) {
    public static UserResponse from(UserResult userResult) {
        return new UserResponse(
                userResult.id(),
                userResult.point()
        );
    }

    public static UserResponse from(UserInfo userInfo) {
        return new UserResponse(
                userInfo.id(),
                userInfo.point()
        );
    }
}
