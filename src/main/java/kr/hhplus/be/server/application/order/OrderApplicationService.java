package kr.hhplus.be.server.application.order;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.order.dto.criteria.OrderCreateCriteria;
import kr.hhplus.be.server.application.order.dto.result.OrderCreateResult;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.dto.info.OrderCreateInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class OrderApplicationService {
    private final OrderService orderService;

    @Transactional
    public OrderCreateResult createOrder(OrderCreateCriteria criteria, LocalDateTime currentTime) {
        OrderCreateInfo orderCreateInfo = orderService.createOrder(criteria.toCommand(), currentTime);
        return OrderCreateResult.from(orderCreateInfo);
    }
}
