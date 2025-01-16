package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.application.order.OrderApplicationService;
import kr.hhplus.be.server.application.order.dto.result.OrderResult;
import kr.hhplus.be.server.interfaces.api.common.ApiResponse;
import kr.hhplus.be.server.interfaces.api.order.dto.request.OrderRequest;
import kr.hhplus.be.server.interfaces.api.order.dto.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    @Operation(summary = "주문 API", description = "주문을 생성한다.")
    @PostMapping("create")
    public ApiResponse<OrderResponse.Order> createOrder(@RequestBody OrderRequest.Order request) {
        OrderResult.Order orderResult = orderApplicationService.order(request.toCriteria());
        return ApiResponse.ok(OrderResponse.Order.of(orderResult));
    }
}
