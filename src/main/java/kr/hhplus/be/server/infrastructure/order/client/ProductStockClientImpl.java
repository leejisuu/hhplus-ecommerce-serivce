package kr.hhplus.be.server.infrastructure.order.client;

import kr.hhplus.be.server.application.order.client.ProductStockClient;
import kr.hhplus.be.server.application.order.dto.criteria.OrderCriteria;
import kr.hhplus.be.server.domain.product.dto.StockCommand;
import kr.hhplus.be.server.domain.product.service.ProductStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductStockClientImpl implements ProductStockClient {

    private final ProductStockService productStockService;

    @Override
    public void reserve(String orderNo, List<OrderCriteria.OrderDetail> orderDetails) {
        StockCommand.Reserve reserveCommand = new StockCommand.Reserve(
                orderNo,
                orderDetails.stream()
                        .map(orderDetail -> new StockCommand.OrderDetail(orderDetail.productId(), orderDetail.quantity()))
                        .toList()
        );

        productStockService.reserve(reserveCommand);
    }

    @Override
    public void confirm(String orderNo) {
        productStockService.confirm(orderNo);
    }

    @Override
    public void cancel(String orderNo) {
        productStockService.cancel(orderNo);
    }
}
