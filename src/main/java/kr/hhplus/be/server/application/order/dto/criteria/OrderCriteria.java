package kr.hhplus.be.server.application.order.dto.criteria;

import kr.hhplus.be.server.domain.order.dto.command.OrderCommand;
import kr.hhplus.be.server.domain.order.dto.command.OrderDetailCommand;
import java.util.List;
import java.util.stream.Collectors;

public record OrderCriteria(
        Long userId,
        List<OrderDetailCriteria> orderDetails) {
    public OrderCommand toCommand() {
        List<OrderDetailCommand> orderDetailCommands = orderDetails.stream()
                .map(OrderDetailCriteria::toCommand) // 변환 로직 호출
                .collect(Collectors.toList());

        return new OrderCommand(userId, orderDetailCommands);
    }
}
