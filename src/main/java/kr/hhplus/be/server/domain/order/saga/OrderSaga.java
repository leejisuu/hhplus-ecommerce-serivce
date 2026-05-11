package kr.hhplus.be.server.domain.order.saga;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import kr.hhplus.be.server.domain.common.saga.SagaStatus;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "order_saga")
public class OrderSaga extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", nullable = false, unique = true)
    private String orderNo;

    @Column(name = "coupon_id")
    private Long couponId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR(30)")
    private SagaStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_step", columnDefinition = "VARCHAR(30)")
    private OrderSagaStepType currentStep;

    @Builder
    private OrderSaga(Long id, String orderNo, SagaStatus status, OrderSagaStepType currentStep, Long couponId) {
        this.id = id;
        this.orderNo = orderNo;
        this.status = status;
        this.currentStep = currentStep;
        this.couponId = couponId;
    }

    public static OrderSaga start(String orderNo, Long couponId) {
        return OrderSaga.builder()
                .orderNo(orderNo)
                .status(SagaStatus.STARTED)
                .couponId(couponId)
                .build();
    }

    public void onStockReserved() {
        if (status != SagaStatus.STARTED) {
            throw new CustomException(ErrorCode.INVALID_SAGA_TRANSITION);
        }
        currentStep = OrderSagaStepType.STOCK_RESERVED;
    }

    public void onCouponReserved() {
        if (status != SagaStatus.STARTED) {
            throw new CustomException(ErrorCode.INVALID_SAGA_TRANSITION);
        }
        currentStep = OrderSagaStepType.COUPON_RESERVED;
    }

    public void onOrderCreated() {
        if (status != SagaStatus.STARTED) {
            throw new CustomException(ErrorCode.INVALID_SAGA_TRANSITION);
        }
        currentStep = OrderSagaStepType.ORDER_CREATED;
    }

    public void onOrderConfirmed() {
        if (status != SagaStatus.STARTED) {
            throw new CustomException(ErrorCode.INVALID_SAGA_TRANSITION);
        }
        currentStep = OrderSagaStepType.ORDER_CONFIRMED;
    }

    public void onOrderFailed() {
        if (status != SagaStatus.STARTED) {
            throw new CustomException(ErrorCode.INVALID_SAGA_TRANSITION);
        }
        currentStep = OrderSagaStepType.ORDER_FAILED;
    }

    public void onCouponReserveCanceled() {
        if (status != SagaStatus.COMPENSATING) {
            throw new CustomException(ErrorCode.INVALID_SAGA_TRANSITION);
        }
        currentStep = OrderSagaStepType.COUPON_RESERVE_CANCELED;
    }

    public void onStockReserveCanceled() {
        if (status != SagaStatus.COMPENSATING) {
            throw new CustomException(ErrorCode.INVALID_SAGA_TRANSITION);
        }
        currentStep = OrderSagaStepType.STOCK_RESERVE_CANCELED;
    }

    public void complete() {
        if (status != SagaStatus.STARTED) {
            throw new CustomException(ErrorCode.INVALID_SAGA_TRANSITION);
        }
        status = SagaStatus.COMPLETED;
    }

    public void startCompensate() {
        if (status != SagaStatus.STARTED) {
            throw new CustomException(ErrorCode.INVALID_SAGA_TRANSITION);
        }
        status = SagaStatus.COMPENSATING;
    }

    public void compensated() {
        if (status != SagaStatus.COMPENSATING) {
            throw new CustomException(ErrorCode.INVALID_SAGA_TRANSITION);
        }
        status = SagaStatus.COMPENSATED;
    }

    public void compensateFailed() {
        if (status != SagaStatus.COMPENSATING) {
            throw new CustomException(ErrorCode.INVALID_SAGA_TRANSITION);
        }
        status = SagaStatus.COMPENSATE_FAILED;
    }

    public boolean isPendingPayment() {
        return status == SagaStatus.STARTED && currentStep == OrderSagaStepType.ORDER_CREATED;
    }
}