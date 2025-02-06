package kr.hhplus.be.server.infrastructure.coupon;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.domain.coupon.entity.QCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CouponCustomRepositoryImpl implements CouponCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public void decreaseRemainCapacity(Long couponId) {
        QCoupon coupon = QCoupon.coupon;

        queryFactory.update(coupon)
                .set(coupon.remainCapacity, coupon.remainCapacity.subtract(1))
                .where(coupon.id.eq(couponId))
                .execute();
    }
}
