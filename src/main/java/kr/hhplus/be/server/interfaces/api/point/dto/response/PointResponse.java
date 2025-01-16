package kr.hhplus.be.server.interfaces.api.point.dto.response;

import kr.hhplus.be.server.domain.point.dto.info.PointInfo;

import java.math.BigDecimal;

public class PointResponse {

    public record Point(
            Long userId,
            BigDecimal point
    ) {
        public static PointResponse.Point of(PointInfo.PointDto pointInfo) {
            return new PointResponse.Point(
                    pointInfo.userId(),
                    pointInfo.point()
            );
        }

    }
}