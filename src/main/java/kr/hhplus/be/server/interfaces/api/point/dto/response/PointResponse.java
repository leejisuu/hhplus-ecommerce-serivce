package kr.hhplus.be.server.interfaces.api.point.dto.response;

import kr.hhplus.be.server.domain.point.dto.info.PointInfo;

import java.math.BigDecimal;

public record PointResponse(
        BigDecimal point
) {
    public static PointResponse of(PointInfo pointInfo) {
        return new PointResponse(
                pointInfo.point()
        );
    }
}
