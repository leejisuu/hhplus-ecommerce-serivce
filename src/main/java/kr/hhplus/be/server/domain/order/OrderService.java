package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.dto.command.OrderCommand;
import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;
import kr.hhplus.be.server.domain.order.dto.info.TopSellingProductInfo;
import kr.hhplus.be.server.domain.order.entity.Order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public List<TopSellingProductInfo> getTopSellingProducts(LocalDate todayDate, int limit) {
        return orderRepository.getTopSellingProducts(todayDate, limit);
    }

    @Transactional
    public OrderInfo order(OrderCommand command, LocalDateTime currentTime) {


        return OrderInfo.of(orderRepository.save(Order.create(null,null,null)));
    }
}
