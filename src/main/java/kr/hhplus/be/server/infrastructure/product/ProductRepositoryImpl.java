package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import kr.hhplus.be.server.domain.product.enums.ProductSellingStatus;
import kr.hhplus.be.server.domain.product.dto.StockDto;
import kr.hhplus.be.server.domain.product.dto.TopSellingProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Page<StockDto> getPagedProducts(Pageable pageable) {
       return productJpaRepository.getPagedProducts(pageable);
    }

    @Override
    public List<TopSellingProductDto> getTopSellingProducts(LocalDate todayDate, int limit) {
        return productJpaRepository.getTopSellingProducts(todayDate, limit);
    }

    @Override
    public List<Product> findAllByIdInAndSellingStatus(List<Long> ids, ProductSellingStatus sellingStatus) {
        return productJpaRepository.findAllByIdInAndSellingStatus(ids, sellingStatus);
    }
}
