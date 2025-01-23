package kr.hhplus.be.server.domain.product.repository;

import kr.hhplus.be.server.domain.product.entity.ProductStock;

public interface ProductStockRepository {

    ProductStock getProductStock(Long productId);

    ProductStock getProductStockWithLock(Long productId);
}
