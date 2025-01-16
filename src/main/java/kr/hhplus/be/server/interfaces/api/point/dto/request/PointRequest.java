package kr.hhplus.be.server.interfaces.api.point.dto.request;

import java.math.BigDecimal;

public class PointRequest {

    public record Charge(
            Long userId,
            BigDecimal amount
    ) {}
}