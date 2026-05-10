package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.entity.ProductStockReservation;
import kr.hhplus.be.server.domain.product.repository.ProductStockReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductStockReservationRepositoryImpl implements ProductStockReservationRepository {

    private final ProductStockReservationJpaRepository productStockReservationJpaRepository;

    @Override
    public List<ProductStockReservation> saveAll(List<ProductStockReservation> reservations) {
        return productStockReservationJpaRepository.saveAll(reservations);
    }

    @Override
    public List<ProductStockReservation> getStockReservations(String orderNo) {
        return productStockReservationJpaRepository.findAllByOrderNo(orderNo);
    }
}
