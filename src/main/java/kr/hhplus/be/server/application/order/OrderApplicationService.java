package kr.hhplus.be.server.application.order;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.order.dto.criteria.OrderCriteria;
import kr.hhplus.be.server.application.order.dto.result.OrderResult;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.dto.command.OrderCommand;
import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class OrderApplicationService {
    private final OrderService orderService;
    private final ProductService productService;

    @Transactional
    public OrderResult order(OrderCriteria criteria, LocalDateTime currentTime) {
        OrderCommand orderCommand = criteria.toCommand();



        OrderInfo orderInfo = orderService.order(criteria.toCommand(), currentTime);
        return OrderResult.from(orderInfo);
    }
}
