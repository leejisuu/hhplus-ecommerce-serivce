package kr.hhplus.be.server.domain.product.entity;

import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ProductStockTest {

    @Test
    void 구매_개수가_보유_재고보다_많으면_CustomException_INSUFFICIENT_STOCK을_발생한다() {
        // given
        Long productId = 1L;
        int hasStock = 1;
        int buyStock = 2;
        ProductStock productStock = ProductStock.create(productId, hasStock);

        // when // then
        assertThatThrownBy(() -> productStock.deductQuantity(buyStock))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INSUFFICIENT_STOCK.getMessage());

    }

    @Test
    void 구매_개수가_보유_재고와_같거나_적으면_재고를_차감한다() {
        // given
        Long productId = 1L;
        int hasStock = 1;
        int buyStock = 1;

        ProductStock productStock = ProductStock.create(productId, hasStock);
        // when
        productStock.deductQuantity(buyStock);

        // then
        assertThat(productStock.getQuantity()).isEqualTo(hasStock - buyStock);
    }
}