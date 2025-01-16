package kr.hhplus.be.server.domain.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 포인트
    POINT_NOT_FOUND(HttpStatus.NOT_FOUND, "포인트 정보가 존재하지 않습니다."),
    INVALID_CHARGE_POINT_AMOUNT(HttpStatus.BAD_REQUEST, "충전 요청 포인트는 0원보다 작을 수 없습니다."),
    INVALID_USE_POINT_AMOUNT(HttpStatus.BAD_REQUEST, "사용 요청 포인트는 0원보다 작을 수 없습니다."),
    INSUFFICIENT_POINT(HttpStatus.BAD_REQUEST, "사용 금액이 보유 잔액보다 클 수 없습니다."),

    // 상품 재고
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "구매 개수가 보유 재고보다 클 수 없습니다."),
    PRODUCT_STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 재고가 존재하지 않습니다."),

    // 상품
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품이 존재하지 않습니다."),

    // 발급 쿠폰
    ALREADY_ISSUED_COUPON(HttpStatus.BAD_REQUEST, "이미 발급받은 쿠폰입니다."),
    ISSUED_COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "발급된 쿠폰이 존재하지 않습니다."),
    COUPON_DISCOUNT_EXCEEDS_NET_AMOUNT(HttpStatus.BAD_REQUEST,"쿠폰 할인 금액이 순수 구매 금액보다 클 수 없습니다."),

    // 쿠폰 마스터
    COUPON_EXPIRED(HttpStatus.BAD_REQUEST, "쿠폰 유효기간이 만료되었습니다."),
    DEACTIVATED_COUPON(HttpStatus.BAD_REQUEST, "발급 불가능한 쿠폰입니다."),
    INSUFFICIENT_COUPON_QUANTITY(HttpStatus.BAD_REQUEST, "쿠폰이 모두 소진되었습니다."),
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "쿠폰이 존재하지 않습니다."),

    // 주문
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문이 존재하지 않습니다."),

    // 유저
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다."),
    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
