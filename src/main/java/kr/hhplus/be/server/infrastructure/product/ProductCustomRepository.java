package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.dto.StockDto;
import kr.hhplus.be.server.domain.product.dto.TopSellingProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ProductCustomRepository {

    Page<StockDto> getPagedProducts(Pageable pageable);

    List<TopSellingProductDto> getTopSellingProducts(LocalDate todayDate, int limit);
}
