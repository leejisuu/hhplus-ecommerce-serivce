package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.entity.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockJpaRepository extends JpaRepository<ProductStock, Long> {
}
