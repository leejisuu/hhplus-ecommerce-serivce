package kr.hhplus.be.server.application.user.dto.result;


import kr.hhplus.be.server.domain.user.dto.info.UserInfo;

public record UserResult(
        Long id,
        String name,
        int point
) {
    public static UserResult from(UserInfo userInfo) {
        return new UserResult(
                userInfo.id(),
                userInfo.name(),
                userInfo.point()
        );
    }
}
