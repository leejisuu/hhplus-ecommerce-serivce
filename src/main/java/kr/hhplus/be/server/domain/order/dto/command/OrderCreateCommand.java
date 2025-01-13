package kr.hhplus.be.server.domain.order.dto.command;

import kr.hhplus.be.server.domain.order.entity.OrderDetail;

import java.util.List;
import java.util.stream.Collectors;

public record OrderCreateCommand(
        Long userId,
        List<OrderDetailCommand> orderDetails,
        Long couponId
) {
}
