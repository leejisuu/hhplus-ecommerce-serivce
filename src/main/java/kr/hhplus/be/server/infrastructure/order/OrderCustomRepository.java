package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.entity.Order;

public interface OrderCustomRepository {
    Order findByIdWithLock(Long orderId);
}
