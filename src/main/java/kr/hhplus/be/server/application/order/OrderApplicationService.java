package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.order.dto.criteria.OrderCriteria;
import kr.hhplus.be.server.application.order.dto.result.OrderResult;
import kr.hhplus.be.server.application.order.event.OrderEventPublisher;
import kr.hhplus.be.server.support.aop.DistributedLock;
import kr.hhplus.be.server.domain.order.service.OrderService;
import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;
import kr.hhplus.be.server.domain.product.service.ProductService;
import kr.hhplus.be.server.domain.product.dto.ProductInfo;
import kr.hhplus.be.server.domain.product.service.ProductStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class OrderApplicationService {
    private final OrderService orderService;
    private final ProductService productService;
    private final ProductStockService productStockService;
    private final OrderEventPublisher orderEventPublisher;

    @DistributedLock(key ="'Order:productIds:' + #criteria.getProductIds()")
    @Transactional
    public OrderResult.Order order(OrderCriteria.Order criteria) {
        List<ProductInfo.ProductDto> products = productService.getProducts(criteria.getProductIds());
        productStockService.deductQuantity(criteria.toStockCommand());
        OrderInfo.OrderDto order = orderService.order(criteria.toCommand(products));
        orderEventPublisher.publish(order.toCreateEvent());
        return OrderResult.Order.of(order);
    }
}