package kr.hhplus.be.server.infrastructure.coupon;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.coupon.repository.IssuedCouponRepository;
import kr.hhplus.be.server.domain.coupon.enums.IssuedCouponStatus;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.entity.QIssuedCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @Override
    public IssuedCoupon saveIssuedCoupon(IssuedCoupon issuedCoupon) {
        return jpaRepository.save(issuedCoupon);
    }

    @Override
    public Page<IssuedCoupon> getAvailableUserCoupons(Long userId, IssuedCouponStatus issuedCouponStatus, LocalDateTime currentTime, Pageable pageable) {
        QIssuedCoupon qIssuedCoupon = QIssuedCoupon.issuedCoupon;

        // IssuedCoupon 목록 조회
        List<IssuedCoupon> content = queryFactory.selectFrom(qIssuedCoupon)
                .where(
                        qIssuedCoupon.user.id.eq(userId),
                        qIssuedCoupon.status.eq(issuedCouponStatus),
                        qIssuedCoupon.validStartedAt.loe(currentTime),
                        qIssuedCoupon.validEndedAt.gt(currentTime)
                )
                .offset(pageable.getOffset()) // offset : 데이터 조회 시 시작 지점 (페이지 번호 * 페이지 크기)
                .limit(pageable.getPageSize()) // pageSize : 한 페이지에 포함될 데이터의 개수
                .fetch();

        /*
        * 카운트 쿼리 최적화 - PageableExecutionUtils 활용
        * 특정 상황에서는 카운트 쿼리를 생략하고도 전체 개수를 추정 가능
        * 1. 첫 번째 페이지이면서, 조회된 데이터의 개수가 페이지 크기보다 작을 때
        *   - 전체 데이터의 개수가 현재 페이지에 조회된 데이터의 개수와 같으므로
        * 2. 마지막 페이지일 때
        *   - 현재 페이지의 오프셋과 조회된 데이터의 개수를 합산하여 전체 데이터의 개수를 추정 가능
        * */
        JPAQuery<Long> countQuery = queryFactory.select(qIssuedCoupon.count())
                .from(qIssuedCoupon)
                .where(
                        qIssuedCoupon.user.id.eq(userId),
                        qIssuedCoupon.status.eq(issuedCouponStatus),
                        qIssuedCoupon.validStartedAt.loe(currentTime),
                        qIssuedCoupon.validEndedAt.gt(currentTime)
                );

        // PageableExecutionUtils를 사용하여 Page 객체 생성
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public IssuedCoupon getIssuedCouponWithLock(Long couponId, Long userId, LocalDateTime currentTime) {
        QIssuedCoupon qIssuedCoupon = QIssuedCoupon.issuedCoupon;

        return queryFactory
                .selectFrom(qIssuedCoupon)
                .where(qIssuedCoupon.coupon.id.eq(couponId),
                        qIssuedCoupon.user.id.eq(userId),
                        qIssuedCoupon.status.eq(IssuedCouponStatus.UNUSED),
                        qIssuedCoupon.validStartedAt.loe(currentTime),
                        qIssuedCoupon.validEndedAt.gt(currentTime)
                        )
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
    }
}
