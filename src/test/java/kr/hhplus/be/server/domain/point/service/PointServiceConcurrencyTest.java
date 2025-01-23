package kr.hhplus.be.server.domain.point.service;

import kr.hhplus.be.server.infrastructure.point.PointJpaRepository;
import kr.hhplus.be.server.support.IntegrationTestSupport;
import kr.hhplus.be.server.domain.point.dto.info.PointInfo;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class PointServiceConcurrencyTest extends IntegrationTestSupport {

    @Autowired
    private PointService pointService;

    @Autowired
    private PointJpaRepository pointJpaRepository;


    @Test
    void 동일한_유저가_포인트_충전을_동시에_신청해도_포인트가_모두_합산된다() throws InterruptedException {
        // given
        Long userId = 1L;
        BigDecimal amount = new BigDecimal(1000);

        PointInfo.PointDto currentPoint = pointService.getPoint(userId);

        int threadCount = 5;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        // when
        for(int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    pointService.charge(userId, amount);
                } catch (CustomException | InterruptedException e) {

                } finally {
                    endLatch.countDown();
                }
            });
        }

        startLatch.countDown();  // 모든 쓰레드 동시에 시작
        endLatch.await();  // 모든 쓰레드가 종료될 때까지 대기
        executorService.shutdown();

        System.out.println();

        // then
        PointInfo.PointDto chargedPoint = pointService.getPoint(userId);

        Assertions.assertThat(chargedPoint.point().compareTo(currentPoint.point().add(amount.multiply(new BigDecimal(threadCount))))).isEqualTo(0);

    }

    @Test
    void 동일한_유저가_포인트_충전을_동시에_신청할때_정상적인_포인트_충전_요청값만_합산된다() throws InterruptedException {
        // given
        Long userId = 1L;
        List<BigDecimal> amounts = Arrays.asList(
                BigDecimal.ZERO,
                new BigDecimal(200),
                new BigDecimal(50),
                BigDecimal.ZERO,
                new BigDecimal(150)
        );

        PointInfo.PointDto currentPoint = pointService.getPoint(userId);

        int threadCount = 5;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        AtomicReference<BigDecimal> successAmt = new AtomicReference<>(BigDecimal.ZERO);

        // when
        for(int i = 0; i < threadCount; i++) {
            BigDecimal amount = amounts.get(i);
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    pointService.charge(userId, amount);
                    successAmt.updateAndGet(v -> v.add(amount));
                } catch (CustomException | InterruptedException e) {

                } finally {
                    endLatch.countDown();
                }
            });
        }

        startLatch.countDown();  // 모든 쓰레드 동시에 시작
        endLatch.await();  // 모든 쓰레드가 종료될 때까지 대기
        executorService.shutdown();

        // then
        PointInfo.PointDto chargedPoint = pointService.getPoint(userId);

        Assertions.assertThat(chargedPoint.point().compareTo(currentPoint.point().add(successAmt.get()))).isEqualTo(0);

    }
}
