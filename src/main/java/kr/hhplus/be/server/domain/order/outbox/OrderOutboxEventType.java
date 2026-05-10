package kr.hhplus.be.server.domain.order.outbox;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderOutboxEventType {
    CONFIRMED("주문 확정")

    ;

    private final String name;
}
