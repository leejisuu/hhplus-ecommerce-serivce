package kr.hhplus.be.server.interfaces.api.coupon;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.dto.info.IssuedCouponInfo;
import kr.hhplus.be.server.interfaces.api.common.ApiResponse;
import kr.hhplus.be.server.interfaces.api.coupon.dto.CouponIssueRequest;
import kr.hhplus.be.server.interfaces.api.coupon.dto.IssuedCouponResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/coupons")
public class CouponController {

    private final CouponService couponService;

    @Operation(summary = "선착순 쿠폰 발급 API", description = "사용자가 쿠폰을 발급받는다.")
    @PostMapping("issue")
    public ApiResponse<IssuedCouponResponse> issueCoupon(@RequestBody CouponIssueRequest couponIssueRequest) {
        LocalDateTime issuedAt = LocalDateTime.now();
        IssuedCouponInfo issuedCouponInfo = couponService.issueCoupon(couponIssueRequest.couponId(), couponIssueRequest.userId(), issuedAt);
        return ApiResponse.ok(IssuedCouponResponse.from(issuedCouponInfo));
    }

    @Operation(summary = "보유 쿠폰 조회 API", description = "유저가 보유한 쿠폰 목록을 반환한다.")
    @PostMapping("/{userId}/available")
    public ApiResponse<Page<IssuedCouponResponse>> getAvailableUserCoupons(@PathVariable Long userId, Pageable pageable) {
        LocalDateTime currentTime = LocalDateTime.now();
        Page<IssuedCouponInfo> map = couponService.getAvailableUserCoupons(userId, currentTime, pageable);
        return ApiResponse.ok(IssuedCouponResponse.mapToResponsePage(map));
    }
}
