package kr.hhplus.be.server.domain.order.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {
    ORDERED("주문 완료"),
    CONFIRMED("주문 확정"),
    FAILED("주문 실패")
    ;

    private final String description;
}