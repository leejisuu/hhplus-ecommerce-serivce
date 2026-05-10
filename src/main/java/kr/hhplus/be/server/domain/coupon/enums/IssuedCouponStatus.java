package kr.hhplus.be.server.domain.coupon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum IssuedCouponStatus {
    AVAILABLE("사용 가능"),
    RESERVED("사용 예약"),
    USED("사용 완료"),
    ;

    private final String description;
}
