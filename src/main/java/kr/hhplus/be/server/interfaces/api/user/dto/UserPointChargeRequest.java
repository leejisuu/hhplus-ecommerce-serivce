package kr.hhplus.be.server.interfaces.api.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserPointChargeRequest {

    private Long userId;
    private int chargeAmt;

    @Builder
    public UserPointChargeRequest(Long userId, int chargeAmt) {
        this.userId = userId;
        this.chargeAmt = chargeAmt;
    }
}
