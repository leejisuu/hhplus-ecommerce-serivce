package kr.hhplus.be.server.interfaces.api.user.dto;

public record UserPointChargeRequest(
        Long userId,
        int chargeAmt
) {
}
