package kr.hhplus.be.server.infrastructure.order;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.order.dto.TopSellingProductInfo;
import kr.hhplus.be.server.domain.order.entity.QOrder;
import kr.hhplus.be.server.domain.order.entity.QOrderDetail;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.entity.QProduct;
import kr.hhplus.be.server.domain.product.entity.QProductStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;


    @Override
    public List<TopSellingProductInfo> getTopSellingProducts(LocalDate todayDate, int limit) {
        QProduct qProduct = QProduct.product; // 상품 테이블
        QProductStock qProductStock = QProductStock.productStock; // 상품 재고 테이블
        QOrder qOrder = QOrder.order; // 주문 테이블
        QOrderDetail qOrderDetail = QOrderDetail.orderDetail; // 주문 상품 테이블

        /*
        * 상품 상품 조건
        * - 전날을 기준으로 3일 동안 판매량이 제일 많은 상품 상위 5개
        * */

        LocalDate startDate = todayDate.minusDays(3);
        LocalDate endDate = todayDate.plusDays(1);

        return queryFactory
                .select(Projections.constructor(
                        TopSellingProductInfo.class,
                        qProduct.id,
                        qProduct.name,
                        qProductStock.quantity,
                        qProduct.price,
                        qOrderDetail.quantity.sum().as("totalSales")
                ))
                .from(qOrderDetail)
                .join(qOrderDetail.product, qProduct)         // OrderDetail → Product 조인
                .join(qProduct.productStock, qProductStock)   // Product → ProductStock 조인
                .join(qOrderDetail.order, qOrder)
                .where(qOrder.createdAt.between(
                        startDate.atStartOfDay(), // atStartOfDay() : 하루의 시작 시간 (00:00:00)으로 변환
                        endDate.atTime(LocalTime.MAX) // atTime(LocalTime.MAX) : 하루의 마지막 시간 (23:59:59.999999999)으로 변환)
                    ),
                    qOrder.status.eq(OrderStatus.COMPLETED)
                )
                .groupBy(qProduct.id)
                .orderBy(qOrderDetail.quantity.sum().desc())
                .limit(limit)
                .fetch();
    }
}
