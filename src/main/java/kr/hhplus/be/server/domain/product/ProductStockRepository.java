package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.product.entity.ProductStock;

public interface ProductStockRepository {

    ProductStock getProductStockWithLock(Long productId);
}
