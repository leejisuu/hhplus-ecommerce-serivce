package kr.hhplus.be.server.application.order.dto;

import kr.hhplus.be.server.interfaces.api.order.dto.OrderDetailRequest;

import java.util.List;

public record OrderCreateParam(
        Long userId,
        List<OrderDetailParam> products,
        Long couponId
) {
}
