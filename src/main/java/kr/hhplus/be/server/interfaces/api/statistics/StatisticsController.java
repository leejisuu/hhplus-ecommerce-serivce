package kr.hhplus.be.server.interfaces.api.statistics;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.interfaces.api.common.ApiResponse;
import kr.hhplus.be.server.interfaces.api.product.dto.ProductResponse;
import kr.hhplus.be.server.interfaces.api.product.dto.ProductsResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Operation(summary = "Get top selling products", description = "상위 인기 상품 목록을 조회한다.")
    @GetMapping("/top-selling-products")
    public ApiResponse<ProductsResponse> topSellingProducts() {

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

        ProductsResponse productsResponse = ProductsResponse.builder()
                .products(List.of(product1, product2, product3))
                .build();

        return ApiResponse.ok(productsResponse);
    }
}
