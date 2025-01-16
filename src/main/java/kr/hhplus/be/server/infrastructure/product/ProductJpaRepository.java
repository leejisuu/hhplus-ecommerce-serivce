package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.enums.ProductSellingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByIdInAndSellingStatus(List<Long> ids, ProductSellingStatus sellingStatus);
}
