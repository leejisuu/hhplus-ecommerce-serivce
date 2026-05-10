package kr.hhplus.be.server.application.order.client;

import kr.hhplus.be.server.application.order.dto.criteria.OrderCriteria;

import java.util.List;

public interface ProductStockClient {
    void reserve(String orderNo, List<OrderCriteria.OrderDetail> orderDetails);

    void confirm(String orderNo);

    void cancel(String orderNo);
}
