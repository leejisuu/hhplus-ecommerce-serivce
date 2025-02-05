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

    // 10분마다 인기상품 캐시 갱신
    @Scheduled(cron = "0 */10 * * * *")
    public void cacheTopSellingProducts() {
        LocalDate todayDate = LocalDate.now();
        int limit = 5;

        cacheTopSellingProducts(todayDate, limit);
    }

    @CachePut(value = "topSellingProducts", key = "#todayDate.toString() + '-' + #limit")
    public void cacheTopSellingProducts(LocalDate todayDate, int limit) {
        productService.getTopSellingProducts(todayDate, limit);
    }
}
