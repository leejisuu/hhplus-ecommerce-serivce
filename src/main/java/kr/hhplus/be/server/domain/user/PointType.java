package kr.hhplus.be.server.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PointType {
    CHARGE("충전"),
    USE("사용");

    private final String title;
}
