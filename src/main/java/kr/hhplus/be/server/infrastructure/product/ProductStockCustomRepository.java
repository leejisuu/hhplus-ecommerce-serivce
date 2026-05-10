package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.entity.ProductStock;

import java.util.List;

public interface ProductStockCustomRepository {
    ProductStock getProductStock(Long productId);

    List<ProductStock> findAllByProductIdInWithLock(List<Long> productIds);
}
