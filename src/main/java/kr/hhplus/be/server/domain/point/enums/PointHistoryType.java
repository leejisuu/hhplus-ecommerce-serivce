package kr.hhplus.be.server.domain.point.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PointHistoryType {
    CHARGE("충전"),
    USE("사용");

    private final String title;
}
