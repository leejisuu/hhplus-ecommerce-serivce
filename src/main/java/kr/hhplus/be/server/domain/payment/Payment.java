package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import kr.hhplus.be.server.domain.order.entity.Order;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Order order;

    private PaymentStatus status;

    @Column(name = "total_amt")
    private BigDecimal totalAmt;

    @Builder
    public Payment(Order order, PaymentStatus status, BigDecimal totalAmt) {
        this.order = order;
        this.status = status;
        this.totalAmt = totalAmt;
    }

    public static Payment create(Order order) {
        return Payment.builder()
                .order(order)
                .status(PaymentStatus.COMPLETED)
                .totalAmt(order.getTotalAmt())
                .build();
    }
}
