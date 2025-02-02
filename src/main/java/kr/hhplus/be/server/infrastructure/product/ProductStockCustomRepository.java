package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.entity.ProductStock;

public interface ProductStockCustomRepository {
    ProductStock getProductStock(Long productId);
}
