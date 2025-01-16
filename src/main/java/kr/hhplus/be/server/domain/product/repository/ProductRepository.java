package kr.hhplus.be.server.domain.product.repository;

import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.enums.ProductSellingStatus;
import kr.hhplus.be.server.infrastructure.product.dto.StockDto;
import kr.hhplus.be.server.infrastructure.product.dto.TopSellingProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Page<StockDto> getPagedProducts(Pageable pageable);

    List<TopSellingProductDto> getTopSellingProducts(LocalDate todayDate, int limit);

    List<Product> findAllByIdInAndSellingStatus(List<Long> productIds, ProductSellingStatus productSellingStatus);
}
