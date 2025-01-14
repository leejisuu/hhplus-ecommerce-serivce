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

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // 순수 구매 금액 (각 상품 재고 곱하기 가격의 합)
    @Column(name = "total_origin_price", nullable = false)
    private BigDecimal totalOriginalPrice;

    @Builder
    private Order(Long userId, OrderStatus status, BigDecimal totalOriginalPrice) {
        this.userId = userId;
        this.status = status;
        this.totalOriginalPrice = totalOriginalPrice;
    }

    public static Order create(Long userId, BigDecimal totalOriginalPrice) {
        return new Order(
                userId,
                OrderStatus.PENDING,
                totalOriginalPrice
        );
    }
}
