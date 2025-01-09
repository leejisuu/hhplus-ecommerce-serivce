package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.interfaces.api.common.ApiResponse;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderCreateRequest;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderCreateResponse;
import kr.hhplus.be.server.interfaces.api.order.dto.TopSellingProductResponse;
import kr.hhplus.be.server.interfaces.api.product.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderFacade orderFacade;
    private final OrderService orderService;

    @Operation(summary = "주문 API", description = "주문을 생성한다.")
    @PostMapping("create")
    public ApiResponse<OrderCreateResponse> createOrder(@RequestBody OrderCreateRequest request) {
        LocalDateTime currentTime = LocalDateTime.now();
        return ApiResponse.ok(orderFacade.createOrder(request.toParam(), currentTime));
    }

    @Operation(summary = "상위 상품 목록 조회 API", description = "상위 인기 상품 목록을 조회한다.")
    @GetMapping("/top-selling-products")
    public ApiResponse<List<TopSellingProductResponse>> getTopSellingProducts() {
        LocalDate todayDate = LocalDate.now();
        int limit = 5;

        return ApiResponse.ok(orderService.getTopSellingProducts(todayDate, limit));
    }
}
