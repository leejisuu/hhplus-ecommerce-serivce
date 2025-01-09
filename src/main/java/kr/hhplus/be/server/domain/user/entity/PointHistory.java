package kr.hhplus.be.server.domain.user.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseCreatedAtEntity;
import kr.hhplus.be.server.domain.user.PointType;
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
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PointType pointType;

    private int amount;

    @Builder
    public PointHistory(User user, PointType pointType, int amount) {
        this.user = user;
        this.pointType = pointType;
        this.amount = amount;
    }

    public static PointHistory create(User user, int amount) {
        return PointHistory.builder()
                .user(user)
                .pointType(PointType.CHARGE)
                .amount(amount)
                .build();
    }
}
