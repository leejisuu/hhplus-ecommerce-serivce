package kr.hhplus.be.server.domain.product.service;

import kr.hhplus.be.server.domain.product.dto.StockCommand;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.entity.ProductStockReservation;
import kr.hhplus.be.server.domain.product.repository.ProductStockRepository;
import kr.hhplus.be.server.domain.product.repository.ProductStockReservationRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductStockService {

    private final ProductStockRepository productStockRepository;
    private final ProductStockReservationRepository productStockReservationRepository;

    @Transactional
    public void reserve(StockCommand.Reserve stockCommand) {
        List<Long> productIds = stockCommand.details().stream()
                .map(StockCommand.OrderDetail::productId)
                .toList();

        List<ProductStock> productStocks = productStockRepository.findAllByProductIdInWithLock(productIds);

        if(stockCommand.details().size() != productStocks.size()) {
            throw new CustomException(ErrorCode.PRODUCT_STOCK_NOT_FOUND);
        }

        Map<Long, ProductStock> stockMap = productStocks.stream()
                .collect(Collectors.toMap(ProductStock::getProductId, stock -> stock));

        List<ProductStockReservation> reservations = new ArrayList<>();
        for(StockCommand.OrderDetail orderDetail : stockCommand.details()) {
            ProductStock productStock = stockMap.get(orderDetail.productId());
            productStock.reserve(orderDetail.quantity());
            reservations.add(ProductStockReservation.create(stockCommand.orderNo(), orderDetail.productId(), orderDetail.quantity()));
        }

        productStockRepository.saveAll(productStocks);
        productStockReservationRepository.saveAll(reservations);
    }

    @Transactional
    public void confirm(String orderNo) {
        List<ProductStockReservation> reservations = productStockReservationRepository.getStockReservations(orderNo);
        reservations.forEach(ProductStockReservation::confirm);
        productStockReservationRepository.saveAll(reservations);
    }

    @Transactional
    public void cancel(String orderNo) {
        List<ProductStockReservation> reservations = productStockReservationRepository.getStockReservations(orderNo);

        List<Long> productIds = reservations.stream().map(ProductStockReservation::getProductId).toList();
        List<ProductStock> productStocks = productStockRepository.findAllByProductIdInWithLock(productIds);

        Map<Long, ProductStock> stockMap = productStocks.stream()
                .collect(Collectors.toMap(ProductStock::getProductId, stock -> stock));

        for(ProductStockReservation reservation : reservations) {
            ProductStock stock = stockMap.get(reservation.getProductId());
            stock.cancel(reservation.getQuantity());
            reservation.cancel();
        }

        productStockRepository.saveAll(productStocks);
        productStockReservationRepository.saveAll(reservations);
    }
}
