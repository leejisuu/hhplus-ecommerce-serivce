package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.product.dto.OrderDetailCommand;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.support.exception.CustomException;
import kr.hhplus.be.server.support.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService; // 테스트 대상

    @Mock
    private ProductRepository productRepository; // Mock 객체

    @Mock
    private ProductStockRepository productStockRepository; // Mock 객체

    @Test
    void 상품_재고_차감시_재고_정보가_존재하지_않는다면_예외를_발생한다() {
        // given
        Long productId = 1L;
        int quantity = 1;

        OrderDetailCommand command = new OrderDetailCommand(productId, quantity);

        given(productStockRepository.getProductStockWithLock(productId))
                .willReturn(null);

        // when // then
        assertThatThrownBy(() -> productService.deductProductStocks(List.of(command)))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.PRODUCT_STOCK_NOT_FOUND.getMessage());

        verify(productStockRepository, times(1)).getProductStockWithLock(productId);
    }

    @Test
    void 상품_재고_차감시_상품_재고_정보가_존재하면_재고_차감_메서드를_호출한다() {
        // given
        Long productId1 = 1L;
        int quantity1 = 3;

        Long productId2 = 2L;
        int quantity2 = 5;

        OrderDetailCommand command1 = new OrderDetailCommand(productId1, quantity1);
        OrderDetailCommand command2 = new OrderDetailCommand(productId2, quantity2);

        ProductStock productStock1 = mock(ProductStock.class);
        ProductStock productStock2 = mock(ProductStock.class);

        // Mock 설정
        given(productStockRepository.getProductStockWithLock(productId1)).willReturn(productStock1);
        given(productStockRepository.getProductStockWithLock(productId2)).willReturn(productStock2);

        // when
        productService.deductProductStocks(List.of(command1, command2));

        // then
        verify(productStockRepository, times(1)).getProductStockWithLock(productId1);
        verify(productStockRepository, times(1)).getProductStockWithLock(productId2);

        verify(productStock1, times(1)).deductStock(quantity1);
        verify(productStock2, times(1)).deductStock(quantity2);
    }

    @Test
    void 상품_순수_구매금액_계산시_상품_정보가_없으면_예외를_발생한다() {
        // given
        Long productId = 1L;
        OrderDetailCommand command = new OrderDetailCommand(productId, 2);

        given(productRepository.getSellingProduct(productId)).willReturn(null);

        // when // then
        assertThatThrownBy(() -> productService.getNetAmt(List.of(command)))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.PRODUCT_NOT_FOUND.getMessage());

        verify(productRepository, times(1)).getSellingProduct(productId);
    }

    @Test
    void 상품_순수_구매금액을_계산한다() {
        // given
        Long productId1 = 1L;
        int productPrice1 = 100;

        Long productId2 = 2L;
        int productPrice2 = 200;

        OrderDetailCommand command1 = new OrderDetailCommand(productId1, 2); // 2개 구매
        OrderDetailCommand command2 = new OrderDetailCommand(productId2, 3); // 3개 구매

        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);

        given(productRepository.getSellingProduct(productId1)).willReturn(product1);
        given(productRepository.getSellingProduct(productId2)).willReturn(product2);

        given(product1.getPrice()).willReturn(productPrice1); // 제품 1 가격: 100
        given(product2.getPrice()).willReturn(productPrice2); // 제품 2 가격: 200

        int calculateAmt = (command1.quantity() * productPrice1) + (command2.quantity() * productPrice2);

        // when
        int netAmt = productService.getNetAmt(List.of(command1, command2));

        // then
        assertThat(netAmt).isEqualTo(calculateAmt);

       verify(productRepository, times(1)).getSellingProduct(productId1);
       verify(productRepository, times(1)).getSellingProduct(productId2);
       verify(product1, times(1)).getPrice();
       verify(product2, times(1)).getPrice();
    }

}