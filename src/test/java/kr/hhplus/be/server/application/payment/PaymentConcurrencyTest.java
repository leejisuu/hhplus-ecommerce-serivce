package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.support.IntegrationTestSupport;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.order.enums.OrderStatus;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentConcurrencyTest extends IntegrationTestSupport {

    @Autowired
    private PaymentApplicationService paymentApplicationService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void 한_유저가_동시에_결제_시_한번만_성공하고_나머지_시도는_실패한다() throws InterruptedException {
        // given
        Long userId = 3L;
        Long orderId = 1L;
        Long issuedCouponId = 2L;
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 10, 0, 0);

        int threadCount = 3;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);

        AtomicInteger successCnt = new AtomicInteger(0);
        AtomicInteger failCnt = new AtomicInteger(0);

        // when
        for(int i = 0; i < threadCount; i++){
            executorService.execute(() -> {
                try {
                        startLatch.await();
                        paymentApplicationService.payment(userId, orderId, issuedCouponId, currentTime);
                        successCnt.incrementAndGet();
                } catch (CustomException | InterruptedException e) {
                        failCnt.incrementAndGet();
                } finally {
                    endLatch.countDown();
                }
            });
        }

        startLatch.countDown();
        endLatch.await();
        executorService.shutdown();

        // then
        Order order = orderRepository.findById(orderId);
        assertThat(order.getStatus().name()).isEqualTo(OrderStatus.PAID.name());

        assertThat(successCnt.get()).isEqualTo(1);
        assertThat(failCnt.get()).isEqualTo(2);
    }
}
