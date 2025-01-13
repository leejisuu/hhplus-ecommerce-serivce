package kr.hhplus.be.server.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {
    PENDING("주문 처리중"),
    COMPLETED("주문 완료"),
    FAILED("주문 실패"),
    CANCELED("주문 취소");

    private final String message;
}
