package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.dto.ProductInfo;
import kr.hhplus.be.server.domain.product.enums.ProductSellingStatus;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import kr.hhplus.be.server.infrastructure.product.dto.StockDto;
import kr.hhplus.be.server.infrastructure.product.dto.TopSellingProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public Page<ProductInfo.Stock> getPagedProducts(Pageable pageable) {
        Page<StockDto> pagedStockDto = productRepository.getPagedProducts(pageable);
        return pagedStockDto.map(ProductInfo.Stock::of);
    }

    public List<ProductInfo.TopSelling> getTopSellingProducts(LocalDate todayDate, int limit) {
        List<TopSellingProductDto> topSellings = productRepository.getTopSellingProducts(todayDate, limit);
        return topSellings.stream()
                .map(ProductInfo.TopSelling::of)
                .collect(Collectors.toList());
    }

    public List<ProductInfo.ProductDto> getProducts(List<Long> productIds) {
        return productRepository.findAllByIdInAndSellingStatus(productIds, ProductSellingStatus.SELLING)
                .stream()
                .map(ProductInfo.ProductDto::of)
                .collect(Collectors.toList());
    }


}
