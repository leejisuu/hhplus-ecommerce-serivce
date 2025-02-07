package kr.hhplus.be.server.interfaces.api.product;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.domain.product.service.ProductService;
import kr.hhplus.be.server.domain.product.dto.ProductInfo;
import kr.hhplus.be.server.interfaces.api.common.ApiResponse;
import kr.hhplus.be.server.interfaces.api.product.dto.ProductResponse;
import kr.hhplus.be.server.interfaces.api.product.dto.TopSellingProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 목록 조회 API", description = "상품 목록을 조회한다.")
    @GetMapping("/selling")
    public ApiResponse<Page<ProductResponse.Stock>> getPagedProducts(Pageable pageable) {
        Page<ProductInfo.Stock> productInfoPage = productService.getPagedProducts(pageable);
        return ApiResponse.ok(productInfoPage.map(ProductResponse.Stock::of));
    }

    @Operation(summary = "상위 상품 목록 조회 API", description = "상위 인기 상품 목록을 조회한다.")
    @GetMapping("/top-selling")
    public ApiResponse<List<TopSellingProductResponse>> getTopSellingProducts() {
        LocalDate todayDate = LocalDate.now();
        int limit = 5;

        List<TopSellingProductResponse> response = productService.getTopSellingProducts(todayDate, limit).stream()
                .map(TopSellingProductResponse::of)
                .collect(Collectors.toList());

        return ApiResponse.ok(response);
    }
}
