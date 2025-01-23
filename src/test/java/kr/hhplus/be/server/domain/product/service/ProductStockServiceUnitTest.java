package kr.hhplus.be.server.domain.product.service;

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
            // given
            Long productId = 11L;
            int quantity = 1;

            given(productStockRepository.getProductStockWithLock(productId)).willReturn(null);

            // when // then
            Assertions.assertThatThrownBy(() -> productStockService.deductQuantity(productId, quantity))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.PRODUCT_STOCK_NOT_FOUND.getMessage());

            verify(productStockRepository, times(1)).getProductStockWithLock(productId);
        }

        @Test
        void 재고_차감_시_구매_개수가_보유_재고_보다_크면_예외를_발생한다() {
            // given
            Long productId = 4L;
            int quantity = 1501;

            ProductStock productStock = ProductStock.create(productId, 1500);

            given(productStockRepository.getProductStockWithLock(productId)).willReturn(productStock);

            // when // then
            Assertions.assertThatThrownBy(() -> productStockService.deductQuantity(productId, quantity))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.INSUFFICIENT_STOCK.getMessage());

            verify(productStockRepository, times(1)).getProductStockWithLock(productId);
        }

        @Test
        void 재고를_치감한다() {
            // given
            Long productId = 1L;
            int quantity = 10;

            ProductStock productStock = ProductStock.create(productId, 9999);

            given(productStockRepository.getProductStockWithLock(productId)).willReturn(productStock);

            // when
            productStockService.deductQuantity(productId, quantity);

            // then
            verify(productStockRepository, times(1)).getProductStockWithLock(productId);
        }
    }
}
