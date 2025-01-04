package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.interfaces.api.common.ApiResponse;
import kr.hhplus.be.server.interfaces.api.coupon.dto.CouponRequest;
import kr.hhplus.be.server.interfaces.api.coupon.dto.CouponResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("coupon")
public class CouponController {

    @Operation(summary = "Issue coupon", description = "사용자가 쿠폰을 발급받는다.")
    @PostMapping("issue")
    public ApiResponse<CouponResponse> issueCoupon(@RequestBody CouponRequest couponRequest) {
        CouponResponse response = CouponResponse.builder()
                .id(1L)
                .name("선착순 쿠폰")
                .discountType("PERCENTAGE")
                .discountAmt(10)
                .validStartAt(LocalDate.of(2025, 1, 1))
                .validEndAt(LocalDate.of(2025, 2, 1))
                .status("NOT USED")
                .build();

        return ApiResponse.ok(response);
    }
}
