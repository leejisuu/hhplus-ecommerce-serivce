package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.ProductSellingStatus;
import kr.hhplus.be.server.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

}
