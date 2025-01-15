package kr.hhplus.be.server.domain.order.dto.command;

import java.util.List;

public record OrderCommand(
        Long userId,
        List<OrderDetailCommand> orderDetails
) {
}
