package kr.hhplus.be.server.domain.point.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.BaseEntity;
import kr.hhplus.be.server.support.exception.CustomException;
import kr.hhplus.be.server.support.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "point", nullable = false)
    private BigDecimal point;

    @Builder
    private Point(Long userId, BigDecimal point) {
        this.userId = userId;
        this.point = point;
    }

    public Point create(Long userId, BigDecimal point) {
        return new Point(userId, point);
    }

    public void addPoint(BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomException(ErrorCode.INVALID_POINT_AMOUNT);
        }

        this.point = this.point.add(amount);
    }

    public void deductPoint(BigDecimal amount) {
        if(this.point.compareTo(amount) < 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_POINT);
        }

        this.point = this.point.subtract(amount);
    }
}
