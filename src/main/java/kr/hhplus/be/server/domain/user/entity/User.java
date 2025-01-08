package kr.hhplus.be.server.domain.user.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.support.exception.CustomException;
import kr.hhplus.be.server.support.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "point", nullable = false)
    private int point;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PointHistory> pointHistories;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IssuedCoupon> userCoupons;

    @Builder
    public User(String name, int point) {
        this.name = name;
        this.point = point;
    }

    public void addPoint(int amount) {
        if(amount <= 0) {
            throw new CustomException(ErrorCode.INVALID_POINT_AMOUNT);
        }

        this.point += amount;
    }

    public void deductPoint(int amount) {
        if(this.point < amount) {
            throw new CustomException(ErrorCode.INSUFFICIENT_POINT);
        }

        this.point -= amount;
    }

    public void addPointHistories(PointHistory pointHistory) {
        // User의 pointHistories 리스트에 추가
        this.pointHistories.add(pointHistory);
        // PointHistory의 user 필드 설정
        pointHistory.setUser(this);
    }
}