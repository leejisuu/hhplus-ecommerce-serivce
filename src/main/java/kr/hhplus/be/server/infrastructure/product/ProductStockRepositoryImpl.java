package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.repository.ProductStockRepository;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductStockRepositoryImpl implements ProductStockRepository {

    private final ProductStockJpaRepository productStockJpaRepository;

    @Override
    public ProductStock getProductStock(Long productId) {
        return productStockJpaRepository.getProductStock(productId);
    }

    @Override
    public ProductStock save(ProductStock productStock) {
        return productStockJpaRepository.save(productStock);
    }

    @Override
    public List<ProductStock> saveAll(List<ProductStock> productStocks) {
        return productStockJpaRepository.saveAll(productStocks);
    }

    @Override
    public List<ProductStock> findAllByProductIdInWithLock(List<Long> productIds) {
        return productStockJpaRepository.findAllByProductIdInWithLock(productIds);
    }
}
