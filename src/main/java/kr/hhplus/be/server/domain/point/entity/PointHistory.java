package kr.hhplus.be.server.domain.point.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseCreatedAtEntity;
import kr.hhplus.be.server.domain.point.enums.PointHistoryType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class PointHistory extends BaseCreatedAtEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="point_id", nullable = false)
    private Long pointId;

    @Enumerated(EnumType.STRING)
    @Column(name = "point_history_type", nullable = false, columnDefinition = "VARCHAR(20)")
    private PointHistoryType pointHistoryType;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Builder
    private PointHistory(Long pointId, PointHistoryType pointHistoryType, BigDecimal amount) {
        this.pointId = pointId;
        this.pointHistoryType = pointHistoryType;
        this.amount = amount;
    }

    public static PointHistory createChargePointHistory(Long pointId, BigDecimal amount) {
        return PointHistory.builder()
                .pointId(pointId)
                .pointHistoryType(PointHistoryType.CHARGE)
                .amount(amount)
                .build();
    }

    public static PointHistory createUsePointHistory(Long pointId, BigDecimal amount) {
        return PointHistory.builder()
                .pointId(pointId)
                .pointHistoryType(PointHistoryType.USE)
                .amount(amount)
                .build();
    }
}
