package kr.hhplus.be.server.infrastructure.product;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.domain.product.entity.ProductStock;
import kr.hhplus.be.server.domain.product.entity.QProduct;
import kr.hhplus.be.server.domain.product.entity.QProductStock;
import kr.hhplus.be.server.domain.product.enums.ProductSellingStatus;
import lombok.RequiredArgsConstructor;

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
}
