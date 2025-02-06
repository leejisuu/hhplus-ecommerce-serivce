package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.dto.ProductInfo;
import kr.hhplus.be.server.domain.product.dto.TopSellingProductsWrapper;
import kr.hhplus.be.server.domain.product.enums.ProductSellingStatus;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import kr.hhplus.be.server.domain.product.dto.StockDto;
import kr.hhplus.be.server.domain.product.dto.TopSellingProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductInfo.Stock> getPagedProducts(Pageable pageable) {
        Page<StockDto> pagedStockDto = productRepository.getPagedProducts(pageable);
        return pagedStockDto.map(ProductInfo.Stock::of);
    }

    @Cacheable(value = "topSellingProducts", key = "'topSellingProducts'")
    public TopSellingProductsWrapper getTopSellingProducts(LocalDate todayDate, int limit) {
        List<TopSellingProductDto> topSellings = productRepository.getTopSellingProducts(todayDate, limit);
        List<ProductInfo.TopSelling> topSellingList = topSellings.stream()
                .map(ProductInfo.TopSelling::of)
                .toList();

        return new TopSellingProductsWrapper(topSellingList);
    }

    public List<ProductInfo.ProductDto> getProducts(List<Long> productIds) {
        return productRepository.findAllByIdInAndSellingStatus(productIds, ProductSellingStatus.SELLING)
                .stream()
                .map(ProductInfo.ProductDto::of)
                .toList();
    }


}
