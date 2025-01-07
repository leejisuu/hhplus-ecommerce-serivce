package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.support.exception.CustomException;
import kr.hhplus.be.server.support.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ProductStockTest {

    @Test
    void 구매_개수가_보유_재고보다_많으면_CustomException을_발생한다() {
        // given
        ProductStock productStock = ProductStock.builder()
                .quantity(1)
                .build();

        Product product = Product.builder()
                .name("상품")
                .status(ProductStatus.SELLING)
                .price(1000)
                .productStock(productStock)
                .build();

        // when // then
        assertThatThrownBy(() -> product.getProductStock().deductStock(2))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INSUFFICIENT_STOCK.getMessage());

    }

    @Test
    void 구매_개수가_보유_재고와_같거나_적으면_재고를_차감한다() {
        // given
        ProductStock productStock = ProductStock.builder()
                .quantity(1)
                .build();

        Product product = Product.builder()
                .name("상품")
                .status(ProductStatus.SELLING)
                .price(1000)
                .productStock(productStock)
                .build();

        // when
        product.getProductStock().deductStock(1);

        // then
        assertThat(product.getProductStock().getQuantity()).isEqualTo(0);
    }
}