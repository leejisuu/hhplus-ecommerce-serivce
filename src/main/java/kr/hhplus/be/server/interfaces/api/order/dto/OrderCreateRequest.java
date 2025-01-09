package kr.hhplus.be.server.interfaces.api.order.dto;

import kr.hhplus.be.server.application.order.dto.OrderCreateParam;
import kr.hhplus.be.server.application.order.dto.OrderDetailParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record OrderCreateRequest (
         Long userId,
         List<OrderDetailRequest> products,
         Long couponId
) {
    public OrderCreateParam toParam() {
        List<OrderDetailParam> orderDetailParams = this.products.stream()
                .map(OrderDetailParam:: of)
                .collect(Collectors.toList());

        return new OrderCreateParam(userId, orderDetailParams, couponId);

    }
}

