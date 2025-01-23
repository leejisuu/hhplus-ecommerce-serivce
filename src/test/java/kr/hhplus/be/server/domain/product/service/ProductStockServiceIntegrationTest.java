package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.support.IntegrationTestSupport;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.repository.ProductStockRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
            Long productId = 11L;
            int quantity = 1;

            // when // then
            Assertions.assertThatThrownBy(() -> productStockService.deductQuantity(productId, quantity))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.PRODUCT_STOCK_NOT_FOUND.getMessage());
        }

        @Test
        void 재고_차감_시_구매_개수가_보유_재고_보다_크면_예외를_발생한다() {
            // given
            Long productId = 4L;
            int quantity = 1501;

            // when // then
            Assertions.assertThatThrownBy(() -> productStockService.deductQuantity(productId, quantity))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.INSUFFICIENT_STOCK.getMessage());
        }

        @Test
        void 재고를_치감한다() {
            // given
            Long productId = 1L;
            int quantity = 1;

            ProductStock originalStock = productStockRepository.getProductStockWithLock(productId);


            // when
            productStockService.deductQuantity(productId, quantity);

            ProductStock productStock = productStockRepository.getProductStockWithLock(productId);

            // then
           Assertions.assertThat(productStock)
                   .extracting("productId", "quantity")
                   .containsExactly(productId, originalStock.getQuantity() - quantity);
        }
    }
}
