package kr.hhplus.be.server.application.order.dto.criteria;

import kr.hhplus.be.server.domain.order.dto.command.OrderCommand;
import kr.hhplus.be.server.domain.product.dto.ProductInfo;
import kr.hhplus.be.server.domain.product.dto.StockCommand;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

public class OrderCriteria {
    public record Order(
            Long userId,
            List<OrderDetail> details
    ) {

        @Builder
        public Order {}

        public StockCommand.OrderDetails toStockCommand() {
            List<StockCommand.OrderDetail> orderDetils = details.stream()
                    .map(orderDetail -> {
                        return StockCommand.OrderDetail.builder()
                                .productId(orderDetail.productId)
                                .quantity(orderDetail.quantity)
                                .build();
                    })
                    .collect(Collectors.toList());

            return new StockCommand.OrderDetails(orderDetils);
        }

        public OrderCommand.Order toCommand(List<ProductInfo.ProductDto> products) {
            List<OrderCommand.OrderDetail> commandOrderDetails = this.details().stream()
                    .map(orderDetail -> {
                        ProductInfo.ProductDto product = products.stream()
                                .filter(p -> p.id().equals(orderDetail.productId()))
                                .findFirst()
                                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
                        return new OrderCommand.OrderDetail(orderDetail.productId(), orderDetail.quantity(), product.price());
                    })
                    .collect(Collectors.toList());

            return new OrderCommand.Order(this.userId(), commandOrderDetails);
        }

        public List<Long> getProductIds() {
            return this.details().stream().map(OrderCriteria.OrderDetail::productId).collect(Collectors.toList());
        }
    }

    public record OrderDetail(
            Long productId,
            int quantity
    ) {
        @Builder
        public OrderDetail {}
    }
}
