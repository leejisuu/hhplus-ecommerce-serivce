package kr.hhplus.be.server.domain.point.dto.info;

import kr.hhplus.be.server.domain.point.entity.Point;

import java.math.BigDecimal;

public record PointInfo(
        BigDecimal point
) {
    public static PointInfo of(Point point) {
        return new PointInfo(point.getPoint());
    }
}
