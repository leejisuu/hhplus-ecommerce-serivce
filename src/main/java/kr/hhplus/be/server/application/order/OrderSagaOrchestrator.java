package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.order.client.CouponClient;
import kr.hhplus.be.server.application.order.client.ProductClient;
import kr.hhplus.be.server.application.order.client.ProductStockClient;
import kr.hhplus.be.server.application.order.dto.criteria.OrderCriteria;
import kr.hhplus.be.server.application.order.dto.result.OrderResult;
import kr.hhplus.be.server.domain.order.OrderNoGenerator;
import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;
import kr.hhplus.be.server.domain.order.saga.OrderSaga;
import kr.hhplus.be.server.domain.order.saga.OrderSagaService;
import kr.hhplus.be.server.domain.order.saga.OrderSagaStepType;
import kr.hhplus.be.server.domain.order.service.OrderService;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static kr.hhplus.be.server.domain.order.saga.OrderSagaStepType.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderSagaOrchestrator {
    private final OrderService orderService;
    private final OrderSagaService orderSagaService;

    private final OrderNoGenerator orderNoGenerator;
    private final OrderValidator orderValidator;
    private final OrderCalculator orderCalculator;

    private final ProductClient productClient;
    private final ProductStockClient productStockClient;
    private final CouponClient couponClient;

    public OrderResult.Create create(OrderCriteria.Create criteria) {
        LocalDate currentDate = LocalDate.now();
        LocalDateTime currentDateTime = LocalDateTime.now();

        String orderNo = orderNoGenerator.generate(currentDate);
        Long couponId = criteria.couponId();

        OrderSaga saga = orderSagaService.start(orderNo, couponId);
        Long sagaId = saga.getId();

        try {
            // 상품 정보 조회
            List<ProductClient.Product> products = productClient.getProducts(criteria.getProductIds());
            orderValidator.validateProducts(criteria, products);

            // 재고 선점
            productStockClient.reserve(orderNo, criteria.details());
            orderSagaService.stockReserved(sagaId);

            Map<Long, BigDecimal> priceMap = orderCalculator.buildPriceMap(products);
            // 주문 상품 금액 계산(할인 미적용)
            BigDecimal totalOriginalAmt = orderCalculator.calculateTotalAmt(criteria, priceMap);

            // 쿠폰 선점 & 할인 금액 계산
            BigDecimal discountAmt = BigDecimal.ZERO;
            if (couponId != null) {
                discountAmt = couponClient.reserve(couponId, totalOriginalAmt, currentDateTime);
                orderSagaService.couponReserved(sagaId);
            }

            // 주문 생성
            OrderInfo.Create order = orderService.order(criteria.toCommand(orderNo, priceMap, totalOriginalAmt, discountAmt));
            orderSagaService.orderCreated(sagaId);
            return OrderResult.Create.of(order);
        } catch (CustomException e) {
            compensate(sagaId);
            throw e;
        }
    }

    public void confirm(String orderNo) {
        OrderInfo.Confirm order = orderService.getOrder(orderNo);

        productStockClient.confirm(order.orderNo());

        if (order.couponId() != null) {
            couponClient.confirm(order.couponId());
        }

        orderService.confirm(orderNo);
        orderSagaService.complete(orderNo);
    }

    public void fail(String orderNo) {
        OrderSaga saga = orderSagaService.findByOrderNo(orderNo);

        if (!saga.isPendingPayment()) {
            return;
        }

        // 주문 실패
        orderService.failed(orderNo);
        orderSagaService.orderFailed(saga.getId());

        compensate(saga.getId());
    }

    private void compensate(Long sagaId) {
        OrderSaga saga = orderSagaService.startCompensate(sagaId);
        OrderSagaStepType currentStep = saga.getCurrentStep();

        if (currentStep == null) {
            orderSagaService.compensated(sagaId);
            return;
        }

        boolean couponSuccess = true;
        boolean stockSuccess = true;

        switch (currentStep) {
            case ORDER_FAILED:
            case COUPON_RESERVED:
                if (saga.getCouponId() != null) {
                    couponSuccess = cancelReservedCoupon(sagaId, saga.getCouponId());
                }
            case STOCK_RESERVED:
                stockSuccess = cancelReservedStock(sagaId, saga.getOrderNo());
                break;
            default:
                throw new CustomException(ErrorCode.INVALID_SAGA_TRANSITION);
        }

        if (couponSuccess && stockSuccess) orderSagaService.compensated(sagaId);
        else orderSagaService.compensateFailed(sagaId);
    }

    private boolean cancelReservedCoupon(Long sagaId, Long couponId) {
        try {
            couponClient.cancel(couponId);
            orderSagaService.couponReserveCancel(sagaId);
            return true;
        } catch (CustomException e) {
            orderSagaService.couponReserveCancelFailed(sagaId);
            log.error("쿠폰 선점 취소 실패, sagaId={}, couponId={}", sagaId, couponId, e);
            return false;
        }
    }

    private boolean cancelReservedStock(Long sagaId, String orderNo) {
        try {
            productStockClient.cancel(orderNo);
            orderSagaService.stockReserveCancel(sagaId);
            return true;
        } catch (CustomException e) {
            orderSagaService.stockReserveCancelFailed(sagaId);
            log.error("재고 선점 취소 실패, sagaId={}, orderNo={}", sagaId, orderNo, e);
            return false;
        }
    }
}
