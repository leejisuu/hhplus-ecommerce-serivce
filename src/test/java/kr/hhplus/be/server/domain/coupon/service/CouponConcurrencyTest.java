package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.dto.CouponDto;
import kr.hhplus.be.server.domain.coupon.dto.command.CouponCommand;
import kr.hhplus.be.server.domain.coupon.dto.info.CouponInfo;
import kr.hhplus.be.server.domain.coupon.repository.CouponCacheRepository;
import kr.hhplus.be.server.support.IntegrationTestSupport;
import kr.hhplus.be.server.domain.coupon.enums.CouponStatus;
import kr.hhplus.be.server.domain.coupon.enums.DiscountType;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class CouponConcurrencyTest extends IntegrationTestSupport {

    @Autowired
    CouponService couponService;
    @Autowired
    CouponWaitingQueueService couponWaitingQueueService;
    @Autowired
    CouponCacheRepository couponCacheRepository;

    @Test
    public void 동시에_동일한_선착순_쿠폰에_대해_10명이_발급_요청_했을_때_5명만_성공한다() throws InterruptedException {
        // given
        int maxCapacity = 5;
        long currentMillis = 1000;
        CouponCommand.Create createCommand = new CouponCommand.Create("웰컴 쿠폰", DiscountType.PERCENTAGE, BigDecimal.valueOf(15), maxCapacity, maxCapacity, LocalDateTime.of(2025, 2, 1, 0, 0, 0), LocalDateTime.of(2025, 2, 28, 23, 59, 59), CouponStatus.ACTIVE);
        CouponInfo.Create coupon = couponService.create(createCommand);

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        AtomicInteger sucessCnt = new AtomicInteger(0);
        AtomicInteger failCnt = new AtomicInteger(0);

        // when
        for (int i = 1; i <= threadCount; i++) {
            long userId = i;

            executorService.submit(() -> {
                try {
                    CouponCommand.AddQueue command = new CouponCommand.AddQueue(coupon.id(), userId, currentMillis+userId);
                    couponWaitingQueueService.addCouponIssueRequest(command);
                    sucessCnt.incrementAndGet();
                } catch (Exception e) {
                    failCnt.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();

        // then
        assertThat(couponCacheRepository.getRemainCapacity(coupon.id())).isEqualTo(0);

        List<CouponDto> coupons = couponCacheRepository.getCouponIssueRequests(10);
        assertThat(coupons).hasSize(maxCapacity);

        assertThat(sucessCnt.get()).isEqualTo(maxCapacity);
        assertThat(failCnt.get()).isEqualTo(threadCount-maxCapacity);
    }
}
