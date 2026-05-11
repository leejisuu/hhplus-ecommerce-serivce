package kr.hhplus.be.server.domain.order.saga;

import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderSagaService {

    private final OrderSagaRepository orderSagaRepository;
    private final OrderSagaStepRepository orderSagaStepRepository;

    @Transactional
    public OrderSaga start(String orderNo, Long couponId) {
        return orderSagaRepository.save(OrderSaga.start(orderNo, couponId));
    }

    @Transactional(readOnly = true)
    public OrderSaga findByOrderNo(String orderNo) {
        return orderSagaRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_SAGA_NOT_FOUND));
    }

    @Transactional
    public OrderSaga startCompensate(Long sagaId) {
        OrderSaga saga = getSaga(sagaId);
        saga.startCompensate();
        return saga;
    }

    @Transactional
    public void compensated(Long sagaId) {
        OrderSaga saga = getSaga(sagaId);
        saga.compensated();
    }

    @Transactional
    public void compensateFailed(Long sagaId) {
        OrderSaga saga = getSaga(sagaId);
        saga.compensateFailed();
    }

    @Transactional
    public void stockReserved(Long sagaId) {
        OrderSaga saga = getSaga(sagaId);
        saga.onStockReserved();
        orderSagaStepRepository.save(OrderSagaStep.success(sagaId, OrderSagaStepType.STOCK_RESERVED));
    }

    @Transactional
    public void couponReserved(Long sagaId) {
        OrderSaga saga = getSaga(sagaId);
        saga.onCouponReserved();
        orderSagaStepRepository.save(OrderSagaStep.success(sagaId, OrderSagaStepType.COUPON_RESERVED));
    }

    @Transactional
    public void orderCreated(Long sagaId) {
        OrderSaga saga = getSaga(sagaId);
        saga.onOrderCreated();
        orderSagaStepRepository.save(OrderSagaStep.success(sagaId, OrderSagaStepType.ORDER_CREATED));
    }

    @Transactional
    public void couponReserveCancel(Long sagaId) {
        OrderSaga saga = getSaga(sagaId);
        saga.onCouponReserveCanceled();
        orderSagaStepRepository.save(OrderSagaStep.success(sagaId, OrderSagaStepType.COUPON_RESERVE_CANCELED));
    }

    @Transactional
    public void couponReserveCancelFailed(Long sagaId) {
        orderSagaStepRepository.save(OrderSagaStep.failed(sagaId, OrderSagaStepType.COUPON_RESERVE_CANCELED));
    }

    @Transactional
    public void stockReserveCancel(Long sagaId) {
        OrderSaga saga = getSaga(sagaId);
        saga.onStockReserveCanceled();
        orderSagaStepRepository.save(OrderSagaStep.success(sagaId, OrderSagaStepType.STOCK_RESERVE_CANCELED));
    }

    @Transactional
    public void stockReserveCancelFailed(Long sagaId) {
        orderSagaStepRepository.save(OrderSagaStep.failed(sagaId, OrderSagaStepType.STOCK_RESERVE_CANCELED));
    }

    @Transactional
    public void orderFailed(Long sagaId) {
        OrderSaga saga = getSaga(sagaId);
        saga.onOrderFailed();
        orderSagaStepRepository.save(OrderSagaStep.success(sagaId, OrderSagaStepType.ORDER_FAILED));
    }

    @Transactional
    public void complete(String orderNo) {
        OrderSaga saga = orderSagaRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_SAGA_NOT_FOUND));
        saga.onOrderConfirmed();
        saga.complete();
        orderSagaStepRepository.save(OrderSagaStep.success(saga.getId(), OrderSagaStepType.ORDER_CONFIRMED));
    }

    private OrderSaga getSaga(Long sagaId) {
        return orderSagaRepository.getOrderSaga(sagaId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_SAGA_NOT_FOUND));
    }
}