package kr.hhplus.be.server.domain.common.outbox;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OutboxStatus {
    INIT("메세지 저장"),
    COMPLETE("발송 성공");

    private final String message;
}

