package kr.hhplus.be.server.domain.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // 순수 구매 금액 (각 상품 재고 곱하기 가격의 합)
    @Column(name = "total_origin_amt", nullable = false)
    private BigDecimal totalOriginalAmt;

    @Builder
    public Order(Long userId, List<OrderDetail> orderDetails, OrderStatus status, BigDecimal totalOriginalAmt) {
        this.userId = userId;
        this.orderDetails = orderDetails;
        this.status = status;
        this.totalOriginalAmt = totalOriginalAmt;
    }

    public static Order create(Long userId, List<OrderDetail> orderDetails, BigDecimal totalOriginalAmt) {
        return new Order(
                userId,
                orderDetails,
                OrderStatus.PENDING,
                totalOriginalAmt
        );
    }
}
