package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum IssuedCouponStatus {
    USED("이미 사용됨"),
    UNUSED("아직 미사용됨");

    private final String name;
}
