package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.dto.ProductInfo;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.enums.ProductSellingStatus;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import kr.hhplus.be.server.infrastructure.product.dto.StockDto;
import kr.hhplus.be.server.infrastructure.product.dto.TopSellingProductDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUnitTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void 재고_정보_포함한_상품_정보를_페이징_처리하여_조회한다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<StockDto> products = List.of(
                new StockDto(1L, "레몬 사탕", new BigDecimal(2500), 9999),
                new StockDto(3L, "청포도 젤리", new BigDecimal(3200), 500),
                new StockDto(5L, "오렌지 초콜릿", new BigDecimal(3500), 800)
                );

        Page<StockDto> pagedStockDto = new PageImpl<>(products, pageable, products.size());

        given(productRepository.getPagedProducts(pageable)).willReturn(pagedStockDto);

        // when
        productService.getPagedProducts(pageable);

        // then
        verify(productRepository).getPagedProducts(pageable);

    }

    @Test
    void 상위_상품을_조회한다() {
        // given
        LocalDate todayDate = LocalDate.of(2025, 1, 1);
        int limit = 5;

        List<TopSellingProductDto> mockTopSellings = List.of(
                new TopSellingProductDto(1L, "레몬 사탕", 9999, BigDecimal.valueOf(2500), 15000L),
                new TopSellingProductDto(3L, "청포도 젤리", 500, BigDecimal.valueOf(3200), 7000L),
                new TopSellingProductDto(4L, "콜라 젤리", 1500, BigDecimal.valueOf(2800), 6500L),
                new TopSellingProductDto(5L, "오렌지 초콜릿", 800, BigDecimal.valueOf(3500), 5000L),
                new TopSellingProductDto(7L, "블루베리 쿠키", 300, BigDecimal.valueOf(4500), 3000L)
                );

        given(productRepository.getTopSellingProducts(todayDate, limit)).willReturn(mockTopSellings);

        // when
        List<ProductInfo.TopSelling> topSellings = productService.getTopSellingProducts(todayDate, limit);

        // then
        assertThat(topSellings).hasSize(limit);

        verify(productRepository).getTopSellingProducts(todayDate, limit);
    }

    @Test
    void 재고_정보_미포함하는_판매중인_상품_목록을_조회한다() {
        // given
        List<Long> productIds = Arrays.asList(1L, 2L, 3L, 4L, 5L);

        List<Product> mockProducts = List.of(
                Product.create("레몬 사탕", ProductSellingStatus.SELLING, new BigDecimal(2500)),
                Product.create("청포도 젤리", ProductSellingStatus.SELLING, new BigDecimal(3200)),
                Product.create("콜라 젤리", ProductSellingStatus.SELLING, new BigDecimal(2800))
        );

        given(productRepository.findAllByIdInAndSellingStatus(productIds, ProductSellingStatus.SELLING)).willReturn(mockProducts);

        // when
        List<ProductInfo.ProductDto> products = productService.getProducts(productIds);

        // then
        assertThat(products).hasSize(3);
        assertThat(products)
                .extracting("name", "price")
                .containsExactly(
                        tuple("레몬 사탕", new BigDecimal(2500)),
                        tuple("청포도 젤리", new BigDecimal(3200)),
                        tuple("콜라 젤리", new BigDecimal(2800))
                        );


        verify(productRepository, times(1)).findAllByIdInAndSellingStatus(productIds, ProductSellingStatus.SELLING);
    }
}
