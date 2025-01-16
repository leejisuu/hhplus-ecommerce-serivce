package kr.hhplus.be.server.domain.point.dto.info;

import kr.hhplus.be.server.domain.point.entity.Point;

import java.math.BigDecimal;

public class PointInfo {

    public record PointDto(
            Long userId,
            BigDecimal point
    ) {
        public static PointInfo.PointDto of(Point point) {
            return new PointInfo.PointDto(
                    point.getUserId(),
                    point.getPoint()
            );
        }
    }
}