package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.dto.TopSellingProductDto;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EnableCaching
@ExtendWith(SpringExtension.class)
public class ProductCacheTest {

    @MockitoSpyBean
    private ProductService productService;

    @MockitoBean
    private ProductRepository productRepository;

    @Test
    void 인기_상품_조회_메서드에_캐시가_적용되어_레포지토리는_한번만_호출되는지_획인한다() {
        // given
        LocalDate todayDate = LocalDate.of(2025, 1, 1);
        int limit = 5;

        List<TopSellingProductDto> mockTopSellings = List.of(
                new TopSellingProductDto(1L, "레몬 사탕", BigDecimal.valueOf(2500), 15000),
                new TopSellingProductDto(3L, "청포도 젤리", BigDecimal.valueOf(3200), 7000),
                new TopSellingProductDto(4L, "콜라 젤리", BigDecimal.valueOf(2800), 6500),
                new TopSellingProductDto(5L, "오렌지 초콜릿", BigDecimal.valueOf(3500), 5000),
                new TopSellingProductDto(7L, "블루베리 쿠키", BigDecimal.valueOf(4500), 3000)
        );

        given(productRepository.getTopSellingProducts(todayDate, limit)).willReturn(mockTopSellings);

        // when
        productService.getTopSellingProducts(todayDate, limit); // DB에서 조회
        productService.getTopSellingProducts(todayDate, limit); // 캐시에서 조회

        // then
        verify(productRepository, times(1)).getTopSellingProducts(todayDate, limit);
        verify(productService, times(2)).getTopSellingProducts(todayDate, limit);
    }
}
