package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.application.order.OrderSagaOrchestrator;
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

    private final OrderSagaOrchestrator orderSagaOrchestrator;

    @Operation(summary = "주문 API", description = "주문을 생성한다.")
    @PostMapping("create")
    public ApiResponse<OrderResponse.Create> createOrder(@RequestBody OrderRequest.Create request) {
        OrderResult.Create createResult = orderSagaOrchestrator.create(request.toCriteria());
        return ApiResponse.ok(OrderResponse.Create.of(createResult));
    }
}
