package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.dto.ProductInfo;
import kr.hhplus.be.server.domain.product.dto.TopSellingProductsWrapper;
import kr.hhplus.be.server.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class ProductServiceIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private ProductService productService;

    @Test
    void 재고_정보_포함한_상품_정보를_페이징_처리하여_조회한다() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<ProductInfo.Stock> productInfoPage = productService.getPagedProducts(pageable);

        // then
        assertThat(productInfoPage.getTotalElements()).isEqualTo(7);
        assertThat(productInfoPage.getContent())
                .extracting("productId", "name", "price", "quantity")
                .containsExactly(
                        tuple(1L, "레몬 사탕", setScaleFromInt(2500), 9999),
                        tuple(3L, "청포도 젤리", setScaleFromInt(3200), 500),
                        tuple(4L, "콜라 젤리", setScaleFromInt(2800), 1500),
                        tuple(5L, "오렌지 초콜릿", setScaleFromInt(3500), 800),
                        tuple(7L, "블루베리 쿠키", setScaleFromInt(4500), 300),
                        tuple(8L, "초코칩 쿠키", setScaleFromInt(3800), 700),
                        tuple(9L, "카라멜 마카롱", setScaleFromInt(5000), 5)
                );
    }

    @Test
    void 재고_정보_미포함하는_판매중인_상품_목록을_조회한다() {
        // given
        List<Long> productIds = Arrays.asList(1L, 2L, 3L, 4L, 5L);

        // when
        List<ProductInfo.ProductDto> products = productService.getProducts(productIds);

        // then
        assertThat(products).hasSize(4);
        assertThat(products)
                .extracting("id", "price")
                .containsExactly(
                        tuple(1L, setScaleFromInt(2500)),
                        tuple(3L, setScaleFromInt(3200)),
                        tuple(4L, setScaleFromInt(2800)),
                        tuple(5L, setScaleFromInt(3500))
                );
    }

    @Test
    void 인기_상품을_조회한다() {
        // given
        LocalDate date = LocalDate.of(2025, 1, 3);

        // when
        TopSellingProductsWrapper wrapper = productService.getTopSellingProducts(date, 5);
        List<ProductInfo.TopSelling> products = wrapper.getTopSellingProducts();

        // then
        assertThat(products).hasSize(2);
        assertThat(products)
                .extracting("productId", "name", "price", "totalQuantity")
                .containsExactly(
                        tuple(9L, "카라멜 마카롱", setScaleFromInt(5000), 10),
                        tuple(8L, "초코칩 쿠키", setScaleFromInt(3800), 3)
                );
    }


    private static BigDecimal setScaleFromInt(int value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
    }

}
