package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.order.dto.criteria.OrderCriteria;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.repository.ProductStockRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderConcurrencyTest extends IntegrationTestSupport {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private ProductStockRepository productStockRepository;

    @Test
    void 동시에_여러_유저가_재고_5개인_상품을_1개씩_구매하면_6번째_구매자는_주문_실패한다() throws InterruptedException {
        // given
        Long userId = 3L;
        List<OrderCriteria.OrderDetail> orderDetailsCriteria = List.of(
                new OrderCriteria.OrderDetail(9L, 1)
        );

        OrderCriteria.Order orderCriteria = new OrderCriteria.Order(userId, orderDetailsCriteria);

        int threadCount = 6;

        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        AtomicInteger successCnt = new AtomicInteger(0);
        AtomicInteger failCnt = new AtomicInteger(0);

        // when
        for(int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderApplicationService.order(orderCriteria);
                    successCnt.incrementAndGet();
                } catch (CustomException e) {
                    failCnt.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();

        // then
        ProductStock stock = productStockRepository.getProductStockWithLock(9L);
        assertThat(stock.getQuantity()).isEqualTo(0);
        assertThat(successCnt.get()).isEqualTo(5);
        assertThat(failCnt.get()).isEqualTo(1);
    }


    @Test
    void 동시에_여러_유저가_재고_5개인_상품을_1개씩_구매하면_재고는_0이_된다() throws InterruptedException {
        // given
        Long userId = 3L;
        List<OrderCriteria.OrderDetail> orderDetailsCriteria = List.of(
                new OrderCriteria.OrderDetail(9L, 1)
        );

        OrderCriteria.Order orderCriteria = new OrderCriteria.Order(userId, orderDetailsCriteria);

        int threadCount = 5;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        AtomicInteger successCnt = new AtomicInteger(0);
        AtomicInteger failCnt = new AtomicInteger(0);

        // when
        for(int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderApplicationService.order(orderCriteria);
                    successCnt.incrementAndGet();
                } catch (CustomException e) {
                    failCnt.incrementAndGet();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();

        // then
        ProductStock stock = productStockRepository.getProductStockWithLock(9L);
        assertThat(stock.getQuantity()).isEqualTo(0);
        assertThat(successCnt.get()).isEqualTo(5);
        assertThat(failCnt.get()).isEqualTo(0);
    }

    @Test
    void 한_유저가_재고_300개인_상품을_100개씩_구매하면_4번째는_주문_실패한다() throws InterruptedException {
        // given
        Long userId = 4L;
        List<OrderCriteria.OrderDetail> orderDetailsCriteria = List.of(
                new OrderCriteria.OrderDetail(7L, 100)
        );

        OrderCriteria.Order orderCriteria = new OrderCriteria.Order(userId, orderDetailsCriteria);

        int threadCount = 4;

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        AtomicInteger successCnt = new AtomicInteger(0);
        AtomicInteger failCnt = new AtomicInteger(0);

        // when
        for(int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    orderApplicationService.order(orderCriteria);
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
        ProductStock stock = productStockRepository.getProductStockWithLock(7L);
        assertThat(stock.getQuantity()).isEqualTo(0);
        assertThat(successCnt.get()).isEqualTo(3);
        assertThat(failCnt.get()).isEqualTo(1);
    }
}
