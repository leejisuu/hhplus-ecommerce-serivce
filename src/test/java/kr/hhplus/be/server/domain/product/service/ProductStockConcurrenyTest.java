package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.repository.ProductStockRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductStockConcurrenyTest extends IntegrationTestSupport {

    @Autowired
    private ProductStockService productStockService;

    @Autowired
    private ProductStockRepository productStockRepository;

    @Test
    void 동시에_여러_유저가_재고_5개인_상품을_1개씩_구매하면_6번째_구매자는_구매를_실패한다() throws InterruptedException {
        // given
        Long productId = 9L;
        int quantity = 1;

        int threadCount = 6;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        AtomicInteger successCnt = new AtomicInteger(0);
        AtomicInteger failCnt = new AtomicInteger(0);

        // when
        for(int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    productStockService.deductQuantity(productId, quantity);
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
        Long productId = 9L;
        int quantity = 1;

        int threadCount = 5;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        AtomicInteger successCnt = new AtomicInteger(0);
        AtomicInteger failCnt = new AtomicInteger(0);

        // when
        for(int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    productStockService.deductQuantity(productId, quantity);
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
}
