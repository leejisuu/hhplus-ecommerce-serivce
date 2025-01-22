package kr.hhplus.be.server.domain.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import kr.hhplus.be.server.domain.order.enums.OrderStatus;
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

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // 순수 구매 금액 (각 상품 재고 곱하기 가격의 합)
    @Column(name = "total_original_amt", nullable = false)
    private BigDecimal totalOriginalAmt;

    @Builder
    public Order(Long userId, OrderStatus status, BigDecimal totalOriginalAmt) {
        this.userId = userId;
        this.status = status;
        this.totalOriginalAmt = totalOriginalAmt;
    }

    public static Order create(Long userId, BigDecimal totalOriginalAmt) {
        return new Order(
                userId,
                OrderStatus.COMPLETED,
                totalOriginalAmt
        );
    }

    public void completePayment() {
        this.status = OrderStatus.PAID;
    }
}
