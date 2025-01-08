package kr.hhplus.be.server.infrastructure.order;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.entity.QOrder;
import kr.hhplus.be.server.domain.order.entity.QOrderDetail;
import kr.hhplus.be.server.domain.product.entity.Product;
import kr.hhplus.be.server.domain.product.entity.QProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpaRepository;
    private final JPAQueryFactory queryFactory;


    @Override
    public List<Product> getTopSellingProducts(LocalDate todayDate, int limit) {
        QProduct product = QProduct.product; // 상품 테이블
        QOrder order = QOrder.order; // 주문 테이블
        QOrderDetail orderDetail = QOrderDetail.orderDetail; // 주문 상품 테이블

        /*
        * 상품 상품 조건
        * - 전날을 기준으로 3일 동안 판매량이 제일 많은 상품 상위 5개
        * */

        LocalDate startDate = todayDate.minusDays(1);
        LocalDate endDate = todayDate.plusDays(2);

        return null;
    }
}
