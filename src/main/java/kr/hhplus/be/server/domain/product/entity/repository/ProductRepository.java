package kr.hhplus.be.server.domain.product.entity.repository;

import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.infrastructure.product.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository {

    Page<ProductDTO> getSellingProducts(Pageable pageable);
}
