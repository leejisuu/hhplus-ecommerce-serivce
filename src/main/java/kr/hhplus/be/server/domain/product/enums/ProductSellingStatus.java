package kr.hhplus.be.server.domain.product.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductSellingStatus {
    SELLING("판매중"),
    STOPPED("판매 종료");

    private final String title;
}
