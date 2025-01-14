package kr.hhplus.be.server.infrastructure.product;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.entity.QProduct;
import kr.hhplus.be.server.domain.product.entity.QProductStock;
import kr.hhplus.be.server.domain.product.entity.repository.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductSellingStatus;
import kr.hhplus.be.server.infrastructure.product.dto.ProductDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProductDTO> getSellingProducts(Pageable pageable) {
        QProduct product = QProduct.product;
        QProductStock productStock = QProductStock.productStock;

        List<ProductDTO> content = queryFactory
                .select(Projections.constructor(
                        ProductDTO.class,
                        product.name,
                        product.price,
                        productStock.quantity
                ))
                .from(product)
                .join(product.id, productStock.productId)
                .where(product.sellingStatus.eq(ProductSellingStatus.SELLING))
                .orderBy(product.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(product.count())
                .from(product)
                .join(product.id, productStock.productId)
                .where(product.sellingStatus.eq(ProductSellingStatus.SELLING));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
