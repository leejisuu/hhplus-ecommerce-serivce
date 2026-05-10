package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.entity.ProductStockReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductStockReservationJpaRepository extends JpaRepository<ProductStockReservation, Long> {
    List<ProductStockReservation> findAllByOrderNo(String orderNo);
}
