package kr.hhplus.be.server.domain.common.saga;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SagaStepStatus {
    SUCCESS("성공"),
    FAILED("실패");

    private final String description;
}