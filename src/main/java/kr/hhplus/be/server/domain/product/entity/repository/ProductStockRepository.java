package kr.hhplus.be.server.domain.product.entity.repository;

import kr.hhplus.be.server.domain.product.entity.ProductStock;

public interface ProductStockRepository {

    ProductStock getProductStockWithLock(Long productId);
}
