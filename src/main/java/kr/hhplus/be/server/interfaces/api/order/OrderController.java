package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.interfaces.api.common.ApiResponse;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderCreateRequest;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderCreateResponse;
import kr.hhplus.be.server.interfaces.api.product.dto.ProductResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Operation(summary = "주문 API", description = "주문을 생성한다.")
    @PostMapping("create")
    public ApiResponse<OrderCreateResponse> createOrder(@RequestBody OrderCreateRequest request) {
        ProductResponse product1 = ProductResponse.builder()
                .id(1L)
                .name("상품1")
                .stock(10)
                .price(1000)
                .build();

        ProductResponse product2 = ProductResponse.builder()
                .id(2L)
                .name("상품2")
                .stock(100)
                .price(5000)
                .build();

        OrderCreateResponse response = OrderCreateResponse.builder()
                .id(1L)
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

    @Operation(summary = "상위 상품 목록 조회 API", description = "상위 인기 상품 목록을 조회한다.")
    @GetMapping("/top-selling-products")
    public ApiResponse<List<ProductResponse>> getTopSellingProducts() {
        ProductResponse product1 = ProductResponse.builder()
                .id(1L)
                .name("상품1")
                .status("판매중")
                .stock(1000)
                .price(9900)
                .build();

        ProductResponse product2 = ProductResponse.builder()
                .id(2L)
                .name("상품2")
                .status("판매중")
                .stock(7)
                .price(19900)
                .build();

        ProductResponse product3 = ProductResponse.builder()
                .id(3L)
                .name("상품3")
                .status("판매중")
                .stock(10)
                .price(3500)
                .build();

        return ApiResponse.ok(List.of(product1, product2, product3));
    }
}
