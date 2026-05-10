package kr.hhplus.be.server.domain.common.saga;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SagaStatus {
    STARTED("시작"),
    COMPLETED("정상 완료"),
    COMPENSATING("보상 트랜잭션 진행중"),
    COMPENSATED("보상 트랜잭션 완료"),
    COMPENSATE_FAILED("보상 트랜잭션 실패"),
    ;

    private final String description;
}