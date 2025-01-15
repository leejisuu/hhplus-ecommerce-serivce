package kr.hhplus.be.server.infrastructure.order;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.order.dto.info.TopSellingProductInfo;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.domain.order.entity.QOrder;
import kr.hhplus.be.server.domain.order.entity.QOrderDetail;
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
        QProduct product = QProduct.product; // 상품 테이블
        QProductStock productStock = QProductStock.productStock; // 상품 재고 테이블
        QOrder order = QOrder.order; // 주문 테이블
        QOrderDetail orderDetail = QOrderDetail.orderDetail; // 주문 상품 테이블


        LocalDate startDate = todayDate.minusDays(3);
        LocalDate endDate = todayDate.plusDays(1);

        /*return queryFactory
                .select(Projections.constructor(
                        TopSellingProductInfo.class,
                        product.id,
                        product.name,
                        productStock.quantity,
                        product.price,
                        orderDetail.quantity.sum().as("totalSales")
                ))
                .from(orderDetail)
                .join(orderDetail.productId.eq(product.id))         // OrderDetail → Product 조인
                .join(product.id.eq(productStock.productId))   // Product → ProductStock 조인
                .join(orderDetail.orderId, order.id)
                .where(order.createdAt.between(
                        startDate.atStartOfDay(), // atStartOfDay() : 하루의 시작 시간 (00:00:00)으로 변환
                        endDate.atTime(LocalTime.MAX) // atTime(LocalTime.MAX) : 하루의 마지막 시간 (23:59:59.999999999)으로 변환)
                    ),
                    order.status.eq(OrderStatus.COMPLETED)
                )
                .groupBy(product.id)
                .orderBy(orderDetail.quantity.sum().desc())
                .limit(limit)
                .fetch();*/

        return null;
    }

    @Override
    public Order save(Order order) {
        return jpaRepository.save(order);
    }

    @Override
    public Order getOrder(Long orderId) {
        return jpaRepository.findById(orderId).orElse(null);
    }
}
