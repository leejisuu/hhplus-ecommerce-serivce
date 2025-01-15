package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.application.order.OrderApplicationService;
import kr.hhplus.be.server.application.order.dto.result.OrderResult;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.interfaces.api.common.ApiResponse;
import kr.hhplus.be.server.interfaces.api.order.dto.request.OrderRequest;
import kr.hhplus.be.server.interfaces.api.order.dto.response.OrderResponse;
import kr.hhplus.be.server.interfaces.api.order.dto.response.TopSellingProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderApplicationService orderApplicationService;
    private final OrderService orderService;

    @Operation(summary = "주문 API", description = "주문을 생성한다.")
    @PostMapping("create")
    public ApiResponse<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        LocalDateTime currentTime = LocalDateTime.now();

        OrderResult OrderResult = orderApplicationService.order(request.toCriteria(), currentTime);
        return ApiResponse.ok(OrderResponse.from(OrderResult));
    }

    @Operation(summary = "상위 상품 목록 조회 API", description = "상위 인기 상품 목록을 조회한다.")
    @GetMapping("/top-selling-products")
    public ApiResponse<List<TopSellingProductResponse>> getTopSellingProducts() {
        LocalDate todayDate = LocalDate.now();
        int limit = 5;

        List<TopSellingProductResponse> topSellingProductsResponse = orderService.getTopSellingProducts(todayDate, limit).stream()
                .map(TopSellingProductResponse::from)
                .collect(Collectors.toList());

        return ApiResponse.ok(topSellingProductsResponse);
    }
}
