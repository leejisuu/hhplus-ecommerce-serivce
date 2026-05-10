package kr.hhplus.be.server.domain.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import kr.hhplus.be.server.domain.order.enums.OrderStatus;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "`order`")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", nullable = false)
    private String orderNo;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "VARCHAR(20)")
    private OrderStatus status;

    // 순수 구매 금액 (각 상품 재고 곱하기 가격의 합)
    @Column(name = "total_original_amt", nullable = false)
    private BigDecimal totalOriginalAmt;

    @Column(name = "discount_amt", nullable = false)
    private BigDecimal discountAmt;

    @Column(name = "final_payment_amt", nullable = false)
    private BigDecimal finalPaymentAmt;

    @Column(name = "used_coupon_id")
    private Long usedCouponId;

    @Builder
    public Order(String orderNo, Long userId, OrderStatus status, BigDecimal totalOriginalAmt, BigDecimal discountAmt, BigDecimal finalPaymentAmt, Long usedCouponId) {
        this.orderNo = orderNo;
        this.userId = userId;
        this.status = status;
        this.totalOriginalAmt = totalOriginalAmt;
        this.discountAmt = discountAmt;
        this.finalPaymentAmt = finalPaymentAmt;
        this.usedCouponId = usedCouponId;
    }

    public static Order create(String orderNo, Long userId, BigDecimal totalOriginalAmt, BigDecimal discountAmt, Long usedCouponId) {
        return Order.builder()
                .orderNo(orderNo)
                .userId(userId)
                .status(OrderStatus.ORDERED)
                .totalOriginalAmt(totalOriginalAmt)
                .discountAmt(discountAmt)
                .finalPaymentAmt(totalOriginalAmt.subtract(discountAmt))
                .usedCouponId(usedCouponId)
                .build();
    }

    public void confirm() {
        this.validateCanPay();
        this.status = OrderStatus.CONFIRMED;
    }

    public void failed() {
        this.validateCanFail();
        this.status = OrderStatus.FAILED;
    }

    public void validateCanPay() {
        if (!this.status.equals(OrderStatus.ORDERED)) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS_FOR_PAYMENT);
        }
    }

    public void validateCanFail() {
        if (!this.status.equals(OrderStatus.ORDERED)) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATUS_FOR_FAIL);
        }
    }
}
