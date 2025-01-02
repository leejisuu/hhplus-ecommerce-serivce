package kr.hhplus.be.server.interfaces.api.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserPointResponse {

    private Long id;
    private int point;

    @Builder
    private UserPointResponse(Long id, int point) {
        this.id = id;
        this.point = point;
    }
}
