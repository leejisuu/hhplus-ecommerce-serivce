package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.DatabaseCleanup;
import kr.hhplus.be.server.IntegrationTestSupport;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.enums.CouponStatus;
import kr.hhplus.be.server.domain.coupon.enums.DiscountType;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import kr.hhplus.be.server.infrastructure.coupon.CouponJpaRepository;
import kr.hhplus.be.server.infrastructure.coupon.IssuedCouponJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CouponServiceIntegrationTest extends IntegrationTestSupport {

    @Autowired
    CouponService couponService;

    @Autowired
    CouponJpaRepository couponJpaRepository;

    @Autowired
    IssuedCouponJpaRepository issuedCouponJpaRepository;

    @Autowired
    DatabaseCleanup databaseCleanup;

    @BeforeEach
    void databaseCleanup() {
        databaseCleanup.execute();
    }

    @Test
    void 쿠폰을_발급한다() {
        // given
        Long userId = 1L;
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 11, 00, 00);
        LocalDateTime validStartedAt = LocalDateTime.of(2025, 1, 1, 00, 00, 00);
        LocalDateTime validEndedAt = LocalDateTime.of(2025, 1, 31, 23, 59, 59);

        Coupon couponInit = Coupon.create("선착순 쿠폰", DiscountType.FIXED_AMOUNT, BigDecimal.valueOf(1000), 30, 30, validStartedAt, validEndedAt, CouponStatus.ACTIVE);
        Coupon savedCoupon = couponJpaRepository.save(couponInit);

        // when
        couponService.issue(savedCoupon.getId(), userId, currentTime);

        // then
        IssuedCoupon issuedCoupon = issuedCouponJpaRepository.findByCouponIdAndUserId(savedCoupon.getId(), userId);
        Coupon coupon = couponJpaRepository.findById(savedCoupon.getId()).orElse(null);

        assertThat(coupon.getRemainCapacity()).isEqualTo(coupon.getMaxCapacity() - 1);
        assertThat(coupon.getId()).isEqualTo(issuedCoupon.getCouponId());
    }

    @Test
    void 쿠폰을_중복_발급하면_예외가_발생한다() {
        // given
        Long userId = 1L;
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 11, 00, 00);
        LocalDateTime validStartedAt = LocalDateTime.of(2025, 1, 1, 00, 00, 00);
        LocalDateTime validEndedAt = LocalDateTime.of(2025, 1, 31, 23, 59, 59);

        Coupon couponInit = Coupon.create("선착순 쿠폰", DiscountType.FIXED_AMOUNT, BigDecimal.valueOf(1000), 30, 30, validStartedAt, validEndedAt, CouponStatus.ACTIVE);
        Coupon savedCoupon = couponJpaRepository.save(couponInit);

        couponService.issue(savedCoupon.getId(), userId, currentTime);

        // when // then
        assertThatThrownBy(() -> couponService.issue(savedCoupon.getId(), userId, currentTime))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ALREADY_ISSUED_COUPON.getMessage());
    }
}
