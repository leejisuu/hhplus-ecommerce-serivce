package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private int discountAmt;

    private int maxCapacity;

    private LocalDateTime validStartedAt;

    private LocalDateTime validEndedAt;

    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    @OneToMany(mappedBy = "coupon")
    private List<IssuedCoupon> issuedCoupons;


}
