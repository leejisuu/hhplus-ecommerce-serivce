package kr.hhplus.be.server.infrastructure.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.domain.product.ProductStockRepository;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.entity.QProductStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductStockRepositoryImpl implements ProductStockRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public ProductStock getProductStockWithLock(Long productId) {
        QProductStock qProductStock = QProductStock.productStock;

        return queryFactory
                .selectFrom(qProductStock)
                .where(qProductStock.id.eq(productId))
                .fetchOne();
    }
}
