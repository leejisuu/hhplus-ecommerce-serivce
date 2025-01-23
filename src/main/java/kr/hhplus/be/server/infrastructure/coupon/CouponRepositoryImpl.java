package kr.hhplus.be.server.infrastructure.coupon;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.coupon.entity.QCoupon;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CouponRepositoryImpl implements CouponRepository {

    private final CouponJpaRepository couponJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Coupon findByIdWithLock(Long couponId) {
        QCoupon coupon = QCoupon.coupon;

        return queryFactory
                .selectFrom(coupon)
                .where(coupon.id.eq(couponId))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
    }

    @Override
    public Coupon findById(Long couponId) {
        return couponJpaRepository.findById(couponId).orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
    }
}
