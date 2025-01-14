package kr.hhplus.be.server.interfaces.api.point.dto.request;

import java.math.BigDecimal;

public record PointChargeRequest(
        Long userId,
        BigDecimal amount
) {
}
