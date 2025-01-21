package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.dto.StockCommand;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.repository.ProductStockRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductStockConcurrenyTest extends IntegrationTestSupport {

    @Autowired
    private ProductStockService productStockService;

    @Autowired
    private ProductStockRepository productStockRepository;

    @Test
    void 동시에_여러_유저가_재고_5개인_상품을_1개씩_구매하면_6번째_구매자는_구매를_실패한다() throws InterruptedException {
        // given
        List<StockCommand.OrderDetail> orderDetails = List.of(
                new StockCommand.OrderDetail(9L, 1)
        );

        StockCommand.OrderDetails stockCommand = new StockCommand.OrderDetails(orderDetails);

        int threadCount = 6;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        AtomicReference<BigDecimal> successCnt = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> failCnt = new AtomicReference<>(BigDecimal.ZERO);

        // when
        for(int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    productStockService.deductQuantity(stockCommand);
                    successCnt.updateAndGet(value -> value.add(BigDecimal.ONE));
                } catch (CustomException e) {
                    failCnt.updateAndGet(value -> value.add(BigDecimal.ONE));
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
        assertThat(successCnt.get().compareTo(new BigDecimal(5))).isEqualTo(0);
        assertThat(failCnt.get().compareTo(new BigDecimal(1))).isEqualTo(0);
    }

    @Test
    void 동시에_여러_유저가_재고_5개인_상품을_1개씩_구매하면_재고는_0이_된다() throws InterruptedException {
        // given
        List<StockCommand.OrderDetail> orderDetails = List.of(
                new StockCommand.OrderDetail(9L, 1)
        );

        StockCommand.OrderDetails stockCommand = new StockCommand.OrderDetails(orderDetails);

        int threadCount = 5;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        AtomicReference<BigDecimal> successCnt = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> failCnt = new AtomicReference<>(BigDecimal.ZERO);

        // when
        for(int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    productStockService.deductQuantity(stockCommand);
                    successCnt.updateAndGet(value -> value.add(BigDecimal.ONE));
                } catch (CustomException e) {
                    failCnt.updateAndGet(value -> value.add(BigDecimal.ONE));
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
        assertThat(successCnt.get().compareTo(new BigDecimal(5))).isEqualTo(0);
        assertThat(failCnt.get().compareTo(new BigDecimal(0))).isEqualTo(0);
    }
}
