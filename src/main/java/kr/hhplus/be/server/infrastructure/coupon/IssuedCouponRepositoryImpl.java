package kr.hhplus.be.server.infrastructure.coupon;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.coupon.entity.QIssuedCoupon;
import kr.hhplus.be.server.domain.coupon.repository.IssuedCouponRepository;
import kr.hhplus.be.server.domain.coupon.enums.IssuedCouponStatus;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class IssuedCouponRepositoryImpl implements IssuedCouponRepository {

    private final IssuedCouponJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;
    private final IssuedCouponJpaRepository issuedCouponJpaRepository;

    @Override
    public IssuedCoupon save(IssuedCoupon issuedCoupon) {
        return jpaRepository.save(issuedCoupon);
    }

    @Override
    public Page<IssuedCoupon> getPagedUserCoupons(Long userId, LocalDateTime currentTime, Pageable pageable) {
        QIssuedCoupon issuedCoupon = QIssuedCoupon.issuedCoupon;

        // IssuedCoupon 목록 조회
        List<IssuedCoupon> content = queryFactory.selectFrom(issuedCoupon)
                .where(
                        issuedCoupon.userId.eq(userId),
                        issuedCoupon.status.eq(IssuedCouponStatus.UNUSED),
                        issuedCoupon.validStartedAt.loe(currentTime),
                        issuedCoupon.validEndedAt.gt(currentTime)
                )
                .offset(pageable.getOffset()) // offset : 데이터 조회 시 시작 지점 (페이지 번호 * 페이지 크기)
                .limit(pageable.getPageSize()) // pageSize : 한 페이지에 포함될 데이터의 개수
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(issuedCoupon.count())
                .from(issuedCoupon)
                .where(
                        issuedCoupon.userId.eq(userId),
                        issuedCoupon.status.eq(IssuedCouponStatus.UNUSED),
                        issuedCoupon.validStartedAt.loe(currentTime),
                        issuedCoupon.validEndedAt.gt(currentTime)
                );

        return new PageImpl<>(content, pageable, countQuery.fetchCount());
    }

    @Override
    public IssuedCoupon getIssuedCouponWithLock(Long issuedCouponId, LocalDateTime currentTime) {
        QIssuedCoupon issuedCoupon = QIssuedCoupon.issuedCoupon;

        return queryFactory
                .selectFrom(issuedCoupon)
                .where(issuedCoupon.id.eq(issuedCouponId),
                        issuedCoupon.status.eq(IssuedCouponStatus.UNUSED),
                        issuedCoupon.validStartedAt.loe(currentTime),
                        issuedCoupon.validEndedAt.gt(currentTime)
                        )
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
    }

    @Override
    public IssuedCoupon findByCouponIdAndUserId(Long couponId, Long userId) {
        return issuedCouponJpaRepository.findByCouponIdAndUserId(couponId, userId);
    }
}
