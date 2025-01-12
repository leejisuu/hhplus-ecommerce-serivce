package kr.hhplus.be.server.domain.coupon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CouponStatus {
    ACTIVE("발급 및 사용 가능"),
    DEACTIVATED("관리자 비활성화");

    private final String message;
}