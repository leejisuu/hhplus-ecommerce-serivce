package kr.hhplus.be.server.domain.user.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.user.PointType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PointHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PointType pointType;

    @Builder
    public PointHistory(User user, PointType pointType) {
        this.user = user;
        this.pointType = pointType;
    }

    public static PointHistory createChargePointHistory(User user) {
        return PointHistory.builder()
                .user(user)
                .pointType(PointType.CHARGE)
                .build();
    }

    public void setUser(User user) {
        this.user = user;
    }
}
