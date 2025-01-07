package kr.hhplus.be.server.support.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 유저 (포인트)
    INVALID_POINT_AMOUNT(HttpStatus.BAD_REQUEST, "충전 요청 포인트는 0원보다 작을 수 없습니다."),
    INSUFFICIENT_POINT(HttpStatus.BAD_REQUEST, "사용 금액이 보유 잔액보다 클 수 없습니다."),

    // 상품 재고
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "구매 개수가 보유 재고보다 클 수 없습니다."),

    // 쿠폰
    INSUFFICIENT_COUPON_QUANTITY(HttpStatus.BAD_REQUEST, "쿠폰이 모두 소진되었습니다."),
    ALREADY_ISSUED_COUPON(HttpStatus.BAD_REQUEST, "이미 발급받은 쿠폰입니다."),
    DEACTIVATED_COUPON(HttpStatus.BAD_REQUEST, "발급 불가능한 쿠폰입니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
