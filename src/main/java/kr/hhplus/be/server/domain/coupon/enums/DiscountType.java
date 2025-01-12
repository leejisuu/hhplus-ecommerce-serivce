package kr.hhplus.be.server.domain.coupon.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum DiscountType {
    PERCENTAGE("정률"),
    FIXED_AMOUNT("정액");

    private final String title;
}
