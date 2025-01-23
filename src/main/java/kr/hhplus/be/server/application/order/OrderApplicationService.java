package kr.hhplus.be.server.application.order;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.order.dto.criteria.OrderCriteria;
import kr.hhplus.be.server.application.order.dto.result.OrderResult;
import kr.hhplus.be.server.domain.dataplatform.DataPlatformClient;
import kr.hhplus.be.server.domain.order.dto.command.OrderCommand;
import kr.hhplus.be.server.domain.order.service.OrderService;
import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;
import kr.hhplus.be.server.domain.product.service.ProductService;
import kr.hhplus.be.server.domain.product.dto.ProductInfo;
import kr.hhplus.be.server.domain.product.service.ProductStockService;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class OrderApplicationService {
    private final OrderService orderService;
    private final ProductService productService;
    private final ProductStockService productStockService;
    private final DataPlatformClient dataPlatformClient;

    @Transactional
    public OrderResult.Order order(OrderCriteria.Order criteria) {
        List<Long> productIds = criteria.details().stream().map(OrderCriteria.OrderDetail::productId).collect(Collectors.toList());
        List<ProductInfo.ProductDto> products = productService.getProducts(productIds);
        OrderCommand.Order orderCommand = convertToOrderCommand(criteria, products);
        for(OrderCommand.OrderDetail detail : orderCommand.details()) {
            productStockService.deductQuantity(detail.productId(), detail.quantity());
        }
        OrderInfo.OrderDto order = orderService.order(orderCommand);

        dataPlatformClient.sendData(order);

        return OrderResult.Order.of(order);
    }

    private OrderCommand.Order convertToOrderCommand(OrderCriteria.Order order, List<ProductInfo.ProductDto> products) {
        List<OrderCommand.OrderDetail> commandOrderDetails = order.details().stream()
                .map(orderDetail -> {
                    ProductInfo.ProductDto product = products.stream()
                            .filter(p -> p.id().equals(orderDetail.productId()))
                            .findFirst()
                            .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
                    return new OrderCommand.OrderDetail(orderDetail.productId(), orderDetail.quantity(), product.price());
                })
                .collect(Collectors.toList());

        return new OrderCommand.Order(order.userId(), commandOrderDetails);
    }
}