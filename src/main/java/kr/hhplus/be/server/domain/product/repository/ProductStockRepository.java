package kr.hhplus.be.server.domain.product.repository;

import kr.hhplus.be.server.domain.product.entity.ProductStock;

import java.util.List;

public interface ProductStockRepository {
    ProductStock getProductStock(Long productId);

    ProductStock save(ProductStock productStock);

    List<ProductStock> saveAll(List<ProductStock> productStocks);

    List<ProductStock> findAllByProductIdInWithLock(List<Long> productIds);
}
