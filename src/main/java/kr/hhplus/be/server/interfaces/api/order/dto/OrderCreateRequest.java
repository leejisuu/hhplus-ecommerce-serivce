package kr.hhplus.be.server.interfaces.api.order.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateRequest {
    private Long userId;
    private List<OrderDetailRequest> products;
    private Long couponId;
}
