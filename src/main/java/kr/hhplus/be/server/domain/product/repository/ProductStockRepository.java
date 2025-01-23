package kr.hhplus.be.server.domain.product.repository;

import kr.hhplus.be.server.domain.product.entity.ProductStock;

public interface ProductStockRepository {

    ProductStock getProductStockWithLock(Long productId);

    ProductStock getProductStock(Long productId);
}
