package kr.hhplus.be.server.interfaces.api.order.dto;

import java.util.List;

public record OrderCreateRequest (
         Long userId,
         List<OrderDetailRequest> products,
         Long couponId
) {
}

