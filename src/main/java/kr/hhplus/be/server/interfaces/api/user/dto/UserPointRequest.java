package kr.hhplus.be.server.interfaces.api.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserPointRequest {

    private Long userId;
    private int chargeAmt;

    @Builder
    public UserPointRequest(Long userId, int chargeAmt) {
        this.userId = userId;
        this.chargeAmt = chargeAmt;
    }
}
