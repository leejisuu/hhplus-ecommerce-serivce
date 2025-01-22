package kr.hhplus.be.server.domain.order.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {
    COMPLETED("주문 완료"),
    FAILED("주문 실패"),
    CANCELED("주문 취소"),
    PAID("결제 완료");

    private final String message;
}
