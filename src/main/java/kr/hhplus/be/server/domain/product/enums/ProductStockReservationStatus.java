package kr.hhplus.be.server.domain.product.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductStockReservationStatus {
    RESERVED("선점"),
    CONFIRMED("확정"),
    CANCELLED("취소");

    private final String description;
}
