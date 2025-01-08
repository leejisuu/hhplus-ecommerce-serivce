package kr.hhplus.be.server.domain.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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
    private int netAmt;

    @Column(name = "discount_amt")
    private int discountAmt;

    @Column(name = "total_amt")
    private int totalAmt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="issued_coupon_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private IssuedCoupon issuedCoupon;

    public Order(User user, OrderStatus status, int netAmt, int discountAmt, int totalAmt, IssuedCoupon issuedCoupon) {
        this.user = user;
        this.status = status;
        this.netAmt = netAmt;
        this.discountAmt = discountAmt;
        this.totalAmt = totalAmt;
        this.issuedCoupon = issuedCoupon;
    }
}
