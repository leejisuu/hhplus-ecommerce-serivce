package kr.hhplus.be.server.infrastructure.point.order;

import kr.hhplus.be.server.domain.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailJpaRepository extends JpaRepository<OrderDetail, Long> {
}
