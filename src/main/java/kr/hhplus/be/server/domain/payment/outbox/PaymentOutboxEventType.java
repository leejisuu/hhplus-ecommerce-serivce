package kr.hhplus.be.server.domain.payment.outbox;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentOutboxEventType {
    COMPLETED("결제 완료"),
    FAILED("결제 실패"),
    ;
    private final String name;
}
