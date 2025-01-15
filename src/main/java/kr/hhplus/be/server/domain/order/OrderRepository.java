package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.dto.info.TopSellingProductInfo;
import kr.hhplus.be.server.domain.order.entity.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository {
    List<TopSellingProductInfo> getTopSellingProducts(LocalDate todayDate, int limit);

    Order save(Order order);

    Order getOrder(Long orderId);
}
