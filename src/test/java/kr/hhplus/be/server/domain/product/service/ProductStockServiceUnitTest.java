package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.dto.StockCommand;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.repository.ProductStockRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class ProductStockServiceUnitTest {

    @InjectMocks
    private ProductStockService productStockService;

    @Mock
    private ProductStockRepository productStockRepository;

    @Nested
    @DisplayName("재고 차감 단위 테스트")
    class DeductQuantity {
        @Test
        void 재고_차감_시_재고_정보가_없으면_예외를_발생한다() {
            Long productId1 = 1L;
            Long productId2 = 3L;
            Long productId3 = 11L;

            // given
            List<StockCommand.OrderDetail> stocks = List.of(
                    new StockCommand.OrderDetail(productId1, 1),
                    new StockCommand.OrderDetail(productId2, 3),
                    new StockCommand.OrderDetail(productId3, 2)
            );

            ProductStock productStock1 = ProductStock.create(productId1, 9999);
            ProductStock productStock2 = ProductStock.create(productId2, 500);

            StockCommand.OrderDetails stockCommand = new StockCommand.OrderDetails(stocks);

            given(productStockRepository.getProductStockWithLock(productId1)).willReturn(productStock1);
            given(productStockRepository.getProductStockWithLock(productId2)).willReturn(productStock2);
            given(productStockRepository.getProductStockWithLock(productId3)).willReturn(null);

            // when // then
            Assertions.assertThatThrownBy(() -> productStockService.deductQuantity(stockCommand))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.PRODUCT_STOCK_NOT_FOUND.getMessage());

            verify(productStockRepository, times(1)).getProductStockWithLock(productId1);
            verify(productStockRepository, times(1)).getProductStockWithLock(productId2);
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

            ProductStock productStock1 = ProductStock.create(productId1, 9999);
            ProductStock productStock2 = ProductStock.create(productId2, 500);
            ProductStock productStock3 = ProductStock.create(productId3, 1500);

            StockCommand.OrderDetails stockCommand = new StockCommand.OrderDetails(stocks);

            given(productStockRepository.getProductStockWithLock(productId1)).willReturn(productStock1);
            given(productStockRepository.getProductStockWithLock(productId2)).willReturn(productStock2);
            given(productStockRepository.getProductStockWithLock(productId3)).willReturn(productStock3);

            // when // then
            Assertions.assertThatThrownBy(() -> productStockService.deductQuantity(stockCommand))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.INSUFFICIENT_STOCK.getMessage());

            verify(productStockRepository, times(1)).getProductStockWithLock(productId1);
            verify(productStockRepository, times(1)).getProductStockWithLock(productId2);
        }

        @Test
        void 재고를_치감한다() {
            Long productId1 = 1L;
            Long productId2 = 3L;
            Long productId3 = 4L;

            // given
            List<StockCommand.OrderDetail> stocks = List.of(
                    new StockCommand.OrderDetail(productId1, 10),
                    new StockCommand.OrderDetail(productId2, 3),
                    new StockCommand.OrderDetail(productId3, 2)
            );

            ProductStock productStock1 = ProductStock.create(productId1, 9999);
            ProductStock productStock2 = ProductStock.create(productId2, 500);
            ProductStock productStock3 = ProductStock.create(productId3, 1500);

            StockCommand.OrderDetails stockCommand = new StockCommand.OrderDetails(stocks);

            given(productStockRepository.getProductStockWithLock(productId1)).willReturn(productStock1);
            given(productStockRepository.getProductStockWithLock(productId2)).willReturn(productStock2);
            given(productStockRepository.getProductStockWithLock(productId3)).willReturn(productStock3);

            // when
            productStockService.deductQuantity(stockCommand);

            // then
            verify(productStockRepository, times(1)).getProductStockWithLock(productId1);
            verify(productStockRepository, times(1)).getProductStockWithLock(productId2);
            verify(productStockRepository, times(1)).getProductStockWithLock(productId3);
        }
    }
}
