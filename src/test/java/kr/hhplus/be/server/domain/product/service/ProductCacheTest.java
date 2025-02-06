package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.dto.ProductInfo;
import kr.hhplus.be.server.domain.product.dto.TopSellingProductDto;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ProductCacheTest {

    @MockitoSpyBean
    private ProductService productService;

    @MockitoBean
    private ProductRepository productRepository;

    @Test
    void 인기_상품_조회_메서드에_캐시가_적용되어_서비스를_여러번_호출했을때_실질적으로_한번만_호출되는지_확인한다() {
        // given
        LocalDate todayDate = LocalDate.of(2025, 1, 1);
        int limit = 5;

        TopSellingProductDto product1 = new TopSellingProductDto(1L, "레몬 사탕", BigDecimal.valueOf(2500), 15000);
        TopSellingProductDto product2 = new TopSellingProductDto(3L, "청포도 젤리", BigDecimal.valueOf(3200), 7000);
        TopSellingProductDto product3 = new TopSellingProductDto(4L, "콜라 젤리", BigDecimal.valueOf(2800), 6500);
        TopSellingProductDto product4 = new TopSellingProductDto(5L, "오렌지 초콜릿", BigDecimal.valueOf(3500), 5000);
        TopSellingProductDto product5 = new TopSellingProductDto(7L, "블루베리 쿠키", BigDecimal.valueOf(4500), 3000);

        List<TopSellingProductDto> mockTopSellings = List.of(product1, product2, product3, product4, product5);

        given(productRepository.getTopSellingProducts(todayDate, limit)).willReturn(mockTopSellings);

        // when
        productService.getTopSellingProducts(todayDate, limit); // DB에서 조회
        List<ProductInfo.TopSelling> topSellings = productService.getTopSellingProducts(todayDate, limit); // 캐시에서 조회

        // then
        Assertions.assertThat(topSellings)
                .extracting("productId", "name", "price", "totalQuantity")
                .containsExactly(
                        Tuple.tuple(product1.getProductId(), product1.getName(), product1.getPrice(), product1.getTotalQuantity()),
                        Tuple.tuple(product2.getProductId(), product2.getName(), product2.getPrice(), product2.getTotalQuantity()),
                        Tuple.tuple(product3.getProductId(), product3.getName(), product3.getPrice(), product3.getTotalQuantity()),
                        Tuple.tuple(product4.getProductId(), product4.getName(), product4.getPrice(), product4.getTotalQuantity()),
                        Tuple.tuple(product5.getProductId(), product5.getName(), product5.getPrice(), product5.getTotalQuantity())
                );

        verify(productRepository, times(1)).getTopSellingProducts(todayDate, limit);
        verify(productService, times(1)).getTopSellingProducts(todayDate, limit);
    }
}
