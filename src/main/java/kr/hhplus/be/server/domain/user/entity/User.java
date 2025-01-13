package kr.hhplus.be.server.domain.user.entity;

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
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "point", nullable = false)
    private BigDecimal point;

    @Builder
    public User(String name, BigDecimal point) {
        this.name = name;
        this.point = point;
    }

    public void addPoint(BigDecimal amount) {
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CustomException(ErrorCode.INVALID_POINT_AMOUNT);
        }

        this.point.add(amount);
    }

    public void deductPoint(BigDecimal amount) {
        if(this.point.compareTo(amount) < 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_POINT);
        }

        this.point.subtract(amount);
    }

}
