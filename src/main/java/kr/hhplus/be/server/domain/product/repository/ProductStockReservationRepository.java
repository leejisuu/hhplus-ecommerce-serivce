package kr.hhplus.be.server.domain.product.repository;

import kr.hhplus.be.server.domain.product.entity.ProductStockReservation;

import java.util.List;

public interface ProductStockReservationRepository {
    List<ProductStockReservation> saveAll(List<ProductStockReservation> reservations);

    List<ProductStockReservation> getStockReservations(String orderNo);
}
