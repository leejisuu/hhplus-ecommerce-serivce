package kr.hhplus.be.server.infrastructure.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.product.entity.QProduct;
import kr.hhplus.be.server.domain.product.enums.ProductSellingStatus;
import kr.hhplus.be.server.domain.product.repository.ProductStockRepository;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.entity.QProductStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductStockRepositoryImpl implements ProductStockRepository {

    private final JPAQueryFactory queryFactory;

    @Transactional
    @Override
    public ProductStock getProductStockWithLock(Long productId) {
        QProduct product = QProduct.product;
        QProductStock productStock = QProductStock.productStock;

        return queryFactory
                .select(productStock)
                .from(productStock)
                .join(product).on(productStock.productId.eq(product.id)) // productStock과 product를 조인
                .where(product.id.eq(productId),
                        product.sellingStatus.eq(ProductSellingStatus.SELLING)
                )
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne();
    }
}
