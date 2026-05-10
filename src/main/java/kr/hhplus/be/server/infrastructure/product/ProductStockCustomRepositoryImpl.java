package kr.hhplus.be.server.infrastructure.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.entity.QProduct;
import kr.hhplus.be.server.domain.product.entity.QProductStock;
import kr.hhplus.be.server.domain.product.enums.ProductSellingStatus;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ProductStockCustomRepositoryImpl implements ProductStockCustomRepository {

    private final JPAQueryFactory queryFactory;
    QProduct product = QProduct.product;
    QProductStock productStock = QProductStock.productStock;

    @Override
    public ProductStock getProductStock(Long productId) {

        return queryFactory
                .select(productStock)
                .from(productStock)
                .join(product).on(productStock.productId.eq(product.id)) // productStock과 product를 조인
                .where(product.id.eq(productId),
                        product.sellingStatus.eq(ProductSellingStatus.SELLING)
                )
                .fetchOne();
    }

    @Override
    public List<ProductStock> findAllByProductIdInWithLock(List<Long> productIds) {
        return queryFactory
                .selectFrom(productStock)
                .where(productStock.productId.in(productIds))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetch();
    }
}
