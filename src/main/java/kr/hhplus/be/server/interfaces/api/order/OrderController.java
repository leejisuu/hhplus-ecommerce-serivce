package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.interfaces.api.common.ApiResponse;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderCreateRequest;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderCreateResponse;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderProductDetailRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("order")
public class OrderController {

    @Operation(summary = "Create order", description = "주문을 생성한다.")
    @PostMapping("create")
    public ApiResponse<OrderCreateResponse> createOrder(@RequestBody OrderCreateRequest request) {
        OrderProductDetailRequest product1 = OrderProductDetailRequest.builder()
                .productId(1L)
                .qantity(2)
                .build();

        OrderProductDetailRequest product2 = OrderProductDetailRequest.builder()
                .productId(2L)
                .qantity(6)
                .build();

        OrderCreateResponse response = OrderCreateResponse.builder()
                .id(1L)
                .userId(1L)
                .status("COMPLETED")
                .netAmt(10000)
                .discountAmt(1000)
                .totalAmt(9000)
                .couponId(3L)
                .products(List.of(product1, product2))
                .createdAt(LocalDateTime.of(2024, 1, 3, 11, 0))
                .build();

        return ApiResponse.ok(response);
    }
}
