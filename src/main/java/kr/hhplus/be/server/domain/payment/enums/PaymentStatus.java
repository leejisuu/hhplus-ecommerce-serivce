package kr.hhplus.be.server.domain.payment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PaymentStatus {
    COMPLETED("결제 완료"),
    FAILED("결제 실패"),
    ;

    private final String description;
}
