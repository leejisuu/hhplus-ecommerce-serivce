package kr.hhplus.be.server.infrastructure.product;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.domain.order.entity.QOrder;
import kr.hhplus.be.server.domain.order.entity.QOrderDetail;
import kr.hhplus.be.server.domain.order.enums.OrderStatus;
import kr.hhplus.be.server.domain.product.entity.QProduct;
import kr.hhplus.be.server.domain.product.entity.QProductStock;
import kr.hhplus.be.server.domain.product.enums.ProductSellingStatus;
import kr.hhplus.be.server.domain.product.dto.StockDto;
import kr.hhplus.be.server.domain.product.dto.TopSellingProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

    private final JPAQueryFactory queryFactory;
    QProduct product = QProduct.product;
    QProductStock productStock = QProductStock.productStock;
    QOrder order = QOrder.order;
    QOrderDetail orderDetail = QOrderDetail.orderDetail;

    @Override
    public Page<StockDto> getPagedProducts(Pageable pageable) {

        List<StockDto> content = queryFactory
                .select(Projections.constructor(
                        StockDto.class,
                        product.id,
                        product.name,
                        product.price,
                        productStock.quantity
                ))
                .from(product)
                .join(productStock).on(product.id.eq(productStock.productId)) // on() 절로 조인
                .where(product.sellingStatus.eq(ProductSellingStatus.SELLING))
                .orderBy(product.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(product.count())
                .from(product)
                .join(productStock).on(product.id.eq(productStock.productId)) // on() 절로 조인
                .where(product.sellingStatus.eq(ProductSellingStatus.SELLING));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public List<TopSellingProductDto> getTopSellingProducts(LocalDate todayDate, int limit) {

        LocalDate startDate = todayDate.minusDays(3);
        LocalDate endDate = todayDate.plusDays(1);

        return queryFactory
                .select(Projections.constructor(
                        TopSellingProductDto.class,
                        product.id,
                        product.name,
                        product.price,
                        orderDetail.quantity.sum().as("totalQuantity")
                ))
                .from(orderDetail)
                .join(orderDetail.order, order)  // OrderDetail → Order 조인
                .join(product).on(orderDetail.productId.eq(product.id))  // OrderDetail → Product 조인
                .leftJoin(productStock).on(product.id.eq(productStock.productId))  // Product → ProductStock 조인
                .where(
                        order.status.eq(OrderStatus.COMPLETED), // 완료된 주문만 조회
                        order.createdAt.between(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX)) // 최근 3일 주문 데이터 조회
                )
                .groupBy(product.id, product.name, product.price)
                .orderBy(orderDetail.quantity.sum().desc()) // 판매량 기준 내림차순 정렬
                .limit(limit)
                .fetch();
    }
}
