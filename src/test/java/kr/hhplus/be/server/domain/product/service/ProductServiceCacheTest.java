package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import java.time.LocalDate;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ProductServiceCacheTest {

    @Autowired
    private ProductService productService;

    @MockitoSpyBean
    private ProductRepository productRepository;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void 인기_상품_조회_메서드에_캐시가_적용되어_서비스를_여러번_호출했을때_레포지토리는_한번만_호출되는지_확인한다() {
        // given
        LocalDate todayDate = LocalDate.of(2025, 1, 1);
        int limit = 5;

        cacheManager.getCache("topSellingProducts").clear();

        // when
        productService.getTopSellingProducts(todayDate, limit);
        productService.getTopSellingProducts(todayDate, limit);

        // then
        verify(productRepository, times(1)).getTopSellingProducts(todayDate, limit);
    }
}
