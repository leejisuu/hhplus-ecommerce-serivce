package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.order.dto.TopSellingProductInfo;
import kr.hhplus.be.server.domain.order.entity.Order;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderCreateResponse;
import kr.hhplus.be.server.interfaces.api.order.dto.TopSellingProductResponse;
import kr.hhplus.be.server.support.exception.CustomException;
import kr.hhplus.be.server.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public List<TopSellingProductResponse> getTopSellingProducts(LocalDate todayDate, int limit) {
        List<TopSellingProductInfo> topSellingProducts = orderRepository.getTopSellingProducts(todayDate, limit);
        return topSellingProducts.stream()
                .map(TopSellingProductResponse::of)
                .collect(Collectors.toList());
    }

    public OrderCreateResponse createOrder(Order order) {
        return OrderCreateResponse.of(orderRepository.createOrder(order));
    }

    public Order getOrder(Long orderId) {
        Order order = orderRepository.getOrder(orderId);
        if(order == null) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }

        return order;
    }
}
