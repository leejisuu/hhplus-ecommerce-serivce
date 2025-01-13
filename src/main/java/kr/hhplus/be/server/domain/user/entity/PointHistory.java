package kr.hhplus.be.server.domain.user.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseCreatedAtEntity;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.user.enums.PointType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PointHistory extends BaseCreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PointType pointType;

    @Column(name = "amount", nullable = false)
    private int amount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Payment payment;

    @Builder
    public PointHistory(User user, PointType pointType, int amount, Payment payment) {
        this.user = user;
        this.pointType = pointType;
        this.amount = amount;
        this.payment = payment;
    }

    public static PointHistory createCharePointHistory(User user, int amount) {
        return PointHistory.builder()
                .user(user)
                .pointType(PointType.CHARGE)
                .amount(amount)
                .build();
    }

    public static PointHistory createUsePointHistory(User user, int amount, Payment payment) {
        return PointHistory.builder()
                .user(user)
                .pointType(PointType.USE)
                .amount(amount)
                .payment(payment)
                .build();
    }
}
