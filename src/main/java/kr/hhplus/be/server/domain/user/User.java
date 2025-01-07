package kr.hhplus.be.server.domain.user;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.point.PointHistory;
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

    private String name;

    private int point;

    @OneToMany(mappedBy = "users")
    private List<PointHistory> pointHistories;

    @OneToMany(mappedBy = "users")
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


}
