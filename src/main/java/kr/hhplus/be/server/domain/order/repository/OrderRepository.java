package kr.hhplus.be.server.domain.order.repository;

import kr.hhplus.be.server.domain.order.dto.info.TopSellingProductInfo;
import kr.hhplus.be.server.domain.order.entity.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository {
    Order save(Order order);

    Order findById(Long orderId);
}
