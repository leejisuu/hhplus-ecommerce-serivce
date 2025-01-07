package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.interfaces.api.common.ApiResponse;
import kr.hhplus.be.server.interfaces.api.coupon.dto.CouponIssueRequest;
import kr.hhplus.be.server.interfaces.api.coupon.dto.IssuedCouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/coupons")
public class CouponController {

    private final CouponFacade couponFacade;

    @Operation(summary = "선착순 쿠폰 발급 API", description = "사용자가 쿠폰을 발급받는다.")
    @PostMapping("issue")
    public ApiResponse<IssuedCouponResponse> issueCoupon(@RequestBody CouponIssueRequest couponIssueRequest) {
        LocalDateTime issuedAt = LocalDateTime.now();

        return ApiResponse.ok(couponFacade.issueCoupon(couponIssueRequest.getCouponId(), couponIssueRequest.getUserId(), issuedAt));
    }
}
