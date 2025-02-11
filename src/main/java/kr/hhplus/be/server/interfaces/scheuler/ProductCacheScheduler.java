package kr.hhplus.be.server.interfaces.scheuler;

import kr.hhplus.be.server.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class ProductCacheScheduler {

    private final ProductService productService;

    // 4시간마다 인기 상품 캐시 갱신
    @CachePut(value = "topSellingProducts", key = "'topSellingProducts'")
    @Scheduled(cron = "0 * */4 * * *")
    public void cachingTopSellingProducts() {
        LocalDate todayDate = LocalDate.now();
        int limit = 5;

        productService.getTopSellingProducts(todayDate, limit);
    }
}
