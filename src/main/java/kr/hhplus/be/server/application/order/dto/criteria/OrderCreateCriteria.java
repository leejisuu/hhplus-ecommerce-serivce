package kr.hhplus.be.server.application.order.dto.criteria;

import kr.hhplus.be.server.domain.order.dto.command.OrderCreateCommand;
import kr.hhplus.be.server.domain.order.dto.command.OrderDetailCommand;

import java.util.List;
import java.util.stream.Collectors;

public record OrderCreateCriteria(
        Long userId,
        List<OrderDetailCriteria> productCriterias,
        Long couponId
) {
    public OrderCreateCommand toCommand() {
        List<OrderDetailCommand> orderDetailCommandList = productCriterias.stream()
                .map(OrderDetailCriteria::toCommand) // 변환 로직 호출
                .collect(Collectors.toList());

        return new OrderCreateCommand(
                userId,
                orderDetailCommandList,
                couponId
        );
    }
}
