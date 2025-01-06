package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.interfaces.api.common.ApiResponse;
import kr.hhplus.be.server.interfaces.api.coupon.dto.CouponIssueRequest;
import kr.hhplus.be.server.interfaces.api.coupon.dto.CouponResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/coupons")
public class CouponController {

    @Operation(summary = "선착순 쿠폰 발급 API", description = "사용자가 쿠폰을 발급받는다.")
    @PostMapping("issue")
    public ApiResponse<CouponResponse> issueCoupon(@RequestBody CouponIssueRequest couponIssueRequest) {
        CouponResponse response = CouponResponse.builder()
                .id(1L)
                .couponId(1L)
                .name("선착순 쿠폰")
                .discountType("PERCENTAGE")
                .discountAmt(10)
                .issuedAt(LocalDateTime.of(2025, 1, 1, 11, 0, 0))
                .validStartAt(LocalDateTime.of(2025, 1, 1, 11, 0, 0))
                .validEndAt(LocalDateTime.of(2025, 2, 1, 11, 0, 0))
                .usedAt(null)
                .status("UNUSED")
                .build();

        return ApiResponse.ok(response);
    }
}
