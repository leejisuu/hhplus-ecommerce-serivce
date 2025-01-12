package kr.hhplus.be.server.interfaces.api.user.dto;

import kr.hhplus.be.server.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

public record UserPointResponse(
        Long id,
        int point
) {
    public static UserPointResponse from(User user) {
        return new UserPointResponse(
                user.getId(),
                user.getPoint()
        );
    }
}
