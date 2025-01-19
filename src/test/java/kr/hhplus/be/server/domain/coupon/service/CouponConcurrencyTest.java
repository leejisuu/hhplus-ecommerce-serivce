package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.IntegrationTestSupport;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.enums.CouponStatus;
import kr.hhplus.be.server.domain.coupon.enums.DiscountType;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.infrastructure.coupon.CouponJpaRepository;
import kr.hhplus.be.server.infrastructure.coupon.IssuedCouponJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

public class CouponConcurrencyTest extends IntegrationTestSupport {

    @Autowired
    CouponService couponService;

    @Autowired
    CouponJpaRepository couponJpaRepository;

    @Autowired
    IssuedCouponJpaRepository issuedCouponJpaRepository;

    @Test
    public void 동시에_동일한_선착순_쿠폰에_대해_40명이_발급했을_때_30명만_성공한다() throws InterruptedException {
        int maxCapacity = 30;

        // given
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 11, 00, 00);
        LocalDateTime validStartedAt = LocalDateTime.of(2025, 1, 1, 00, 00, 00);
        LocalDateTime validEndedAt = LocalDateTime.of(2025, 1, 31, 23, 59, 59);

        Coupon couponInfo = Coupon.create("선착순 쿠폰", DiscountType.FIXED_AMOUNT, BigDecimal.valueOf(1000), maxCapacity, maxCapacity, validStartedAt, validEndedAt, CouponStatus.ACTIVE);
        Coupon savedCoupon = couponJpaRepository.save(couponInfo);

        int threadCount = 40;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 1; i <= threadCount; i++) {
            long userId = i;

            executorService.submit(() -> {
                try {
                        couponService.issue(savedCoupon.getId(), userId, currentTime);
                } catch (Exception e) {
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();

        Coupon soldOutCoupon = couponJpaRepository.findById(savedCoupon.getId()).orElse(null);
        List<IssuedCoupon> coupons = issuedCouponJpaRepository.findAllByCouponId(savedCoupon.getId());

        // then
        // 쿠폰 모두 소진됨
        assertThat(soldOutCoupon.getRemainCapacity()).isEqualTo(0);
        // 발급된 쿠폰 개수가 최대 발행 개수랑 동일함
        assertThat(coupons.size()).isEqualTo(maxCapacity);
    }

    @Test
    public void 동일한_유저가_선착순_쿠폰을_5번_발급신청하면_1번만_성공한다() throws InterruptedException {
        int maxCapacity = 10;
        Long userId = 1L;

        // given
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 11, 00, 00);
        LocalDateTime validStartedAt = LocalDateTime.of(2025, 1, 1, 00, 00, 00);
        LocalDateTime validEndedAt = LocalDateTime.of(2025, 1, 31, 23, 59, 59);

        Coupon couponInfo = Coupon.create("선착순 쿠폰", DiscountType.FIXED_AMOUNT, BigDecimal.valueOf(1000), maxCapacity, maxCapacity, validStartedAt, validEndedAt, CouponStatus.ACTIVE);
        Coupon savedCoupon = couponJpaRepository.save(couponInfo);

        int threadCount = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(1);

        // when
        for (int i = 1; i <= threadCount; i++) {
            executorService.submit(() -> {
                try {
                    couponService.issue(savedCoupon.getId(), userId, currentTime);
                } catch (CustomException e) {
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();

        List<IssuedCoupon> coupons = issuedCouponJpaRepository.findAllByCouponIdAndUserId(savedCoupon.getId(), userId);

        // then
        // 유저가 발급 받은 쿠폰은 하나
        assertThat(coupons.size()).isEqualTo(1);
    }
}
