package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.support.IntegrationTestSupport;
import kr.hhplus.be.server.domain.product.dto.StockCommand;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.repository.ProductStockRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProductStockServiceIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private ProductStockService productStockService;

    @Autowired
    private ProductStockRepository productStockRepository;

    @Nested
    @DisplayName("재고 차감 통합 테스트")
    class DeductQuantity {
        @Test
        void 재고_차감_시_재고_정보가_없으면_예외를_발생한다() {
            // given
            Long productId1 = 1L;
            Long productId2 = 3L;
            Long productId3 = 11L;

            // given
            List<StockCommand.OrderDetail> stocks = List.of(
                    new StockCommand.OrderDetail(productId1, 10),
                    new StockCommand.OrderDetail(productId2, 3),
                    new StockCommand.OrderDetail(productId3, 2)
            );

            StockCommand.OrderDetails stockCommand = new StockCommand.OrderDetails(stocks);

            // when // then
            Assertions.assertThatThrownBy(() -> productStockService.deductQuantity(stockCommand))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.PRODUCT_STOCK_NOT_FOUND.getMessage());
        }

        @Test
        void 재고_차감_시_구매_개수가_보유_재고_보다_크면_예외를_발생한다() {
            Long productId1 = 1L;
            Long productId2 = 3L;
            Long productId3 = 4L;

            // given
            List<StockCommand.OrderDetail> stocks = List.of(
                    new StockCommand.OrderDetail(productId1, 1),
                    new StockCommand.OrderDetail(productId2, 3),
                    new StockCommand.OrderDetail(productId3, 1501)
            );

            StockCommand.OrderDetails stockCommand = new StockCommand.OrderDetails(stocks);

            // when // then
            Assertions.assertThatThrownBy(() -> productStockService.deductQuantity(stockCommand))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.INSUFFICIENT_STOCK.getMessage());
        }

        @Test
        void 재고를_치감한다() {
            // given
            Long productId1 = 1L;
            Long productId2 = 3L;
            Long productId3 = 4L;

            int stock1 = 9999;
            int stock2 = 500;
            int stock3 = 1500;

            int quantity1 = 1;
            int quantity2 = 500;
            int quantity3 = 1499;

            // given
            List<StockCommand.OrderDetail> stocks = List.of(
                    new StockCommand.OrderDetail(productId1, quantity1),
                    new StockCommand.OrderDetail(productId2, quantity2),
                    new StockCommand.OrderDetail(productId3, quantity3)
            );

            StockCommand.OrderDetails stockCommand = new StockCommand.OrderDetails(stocks);

            // when
            productStockService.deductQuantity(stockCommand);

            ProductStock productStock1 = productStockRepository.getProductStockWithLock(productId1);
            ProductStock productStock2 = productStockRepository.getProductStockWithLock(productId2);
            ProductStock productStock3 = productStockRepository.getProductStockWithLock(productId3);

            // then
           Assertions.assertThat(productStock1)
                   .extracting("productId", "quantity")
                   .containsExactly(productId1, stock1 - quantity1);

            Assertions.assertThat(productStock2)
                    .extracting("productId", "quantity")
                    .containsExactly(productId2, stock2 - quantity2);

            Assertions.assertThat(productStock3)
                    .extracting("productId", "quantity")
                    .containsExactly(productId3, stock3 - quantity3);
        }
    }
}
