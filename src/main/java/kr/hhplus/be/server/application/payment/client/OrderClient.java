package kr.hhplus.be.server.application.payment.client;

import java.math.BigDecimal;

public interface OrderClient {
    OrderDto getOrder(String orderNo);

    record OrderDto(String orderNo, BigDecimal paymentAmt) {}
}
