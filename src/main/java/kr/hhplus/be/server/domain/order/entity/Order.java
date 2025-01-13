package kr.hhplus.be.server.domain.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.user.entity.User;
import lombok.AccessLevel;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "net_amt")
    private BigDecimal netAmt;

    @Column(name = "discount_amt")
    private BigDecimal discountAmt;

    @Column(name = "total_amt")
    private BigDecimal totalAmt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="issued_coupon_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private IssuedCoupon issuedCoupon;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public Order(User user, OrderStatus status, BigDecimal netAmt, BigDecimal discountAmt, BigDecimal totalAmt, IssuedCoupon issuedCoupon, List<OrderDetail> orderDetails) {
        this.user = user;
        this.status = status;
        this.netAmt = netAmt;
        this.discountAmt = discountAmt;
        this.totalAmt = totalAmt;
        this.issuedCoupon = issuedCoupon;
        this.orderDetails = orderDetails;
    }

    public static Order create(User user, IssuedCoupon issuedCoupon, List<OrderDetail> orderDetails, LocalDateTime currentTime) {
        // 순수 구매 금액 합
        BigDecimal netAmt = calculateNetAmt(orderDetails);

        // 쿠폰 할인 금액 계산 및 쿠폰 사용 처리
        BigDecimal discountAmt = BigDecimal.ZERO;
        if(!ObjectUtils.isEmpty(issuedCoupon)) {
            discountAmt = issuedCoupon.calculateDiscountAmt(netAmt);
            issuedCoupon.useIssuedCoupon(currentTime);
        }

        Order order = new Order(
                user,
                OrderStatus.PENDING,
                netAmt,
                discountAmt,
                netAmt.subtract(discountAmt),
                issuedCoupon,
                orderDetails
        );

        // 주문과 주문 상품 연관 관계 세팅
        orderDetails.forEach(order::addOrderDetail);

        return order;
    }

    private void addOrderDetail(OrderDetail orderDetail) {
        this.orderDetails.add(orderDetail);
        orderDetail.setOrder(this);
    }

    private static BigDecimal calculateNetAmt(List<OrderDetail> orderDetails) {
        return orderDetails.stream()
                .map(OrderDetail::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add); // BigDecimal 합산
    }
}
