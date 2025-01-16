package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.entity.OrderDetail;
import kr.hhplus.be.server.domain.order.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderDetailRepositoryImpl implements OrderDetailRepository {
    private final OrderDetailJpaRepository orderDetailJpaRepository;

    @Override
    public List<OrderDetail> saveAll(List<OrderDetail> orderDetails) {
        return orderDetailJpaRepository.saveAll(orderDetails);
    }
}
