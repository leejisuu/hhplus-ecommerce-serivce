package kr.hhplus.be.server.domain.order.saga;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderSagaStepType {
    STOCK_RESERVED("재고 선점"),
    COUPON_RESERVED("쿠폰 선점"),
    ORDER_CREATED("주문 생성"),
    ORDER_CONFIRMED("주문 확정"),

    STOCK_RESERVE_CANCELED("재고 선점 취소"),
    COUPON_RESERVE_CANCELED("쿠폰 선점 취소"),
    ORDER_FAILED("주문 취소(결제 실패)"),
    ;

    private final String description;
}
