package kr.hhplus.be.server.domain.order.saga;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseCreatedAtEntity;
import kr.hhplus.be.server.domain.common.saga.SagaStepStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "order_saga_step")
public class OrderSagaStep extends BaseCreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "saga_id", nullable = false)
    private Long sagaId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "VARCHAR(30)")
    private OrderSagaStepType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR(10)")
    private SagaStepStatus status;

    @Builder
    private OrderSagaStep(Long sagaId, OrderSagaStepType type, SagaStepStatus status) {
        this.sagaId = sagaId;
        this.type = type;
        this.status = status;
    }

    public static OrderSagaStep success(Long sagaId, OrderSagaStepType type) {
        return OrderSagaStep.builder()
                .sagaId(sagaId)
                .type(type)
                .status(SagaStepStatus.SUCCESS)
                .build();
    }

    public static OrderSagaStep failed(Long sagaId, OrderSagaStepType type) {
        return OrderSagaStep.builder()
                .sagaId(sagaId)
                .type(type)
                .status(SagaStepStatus.FAILED)
                .build();
    }
}