package kr.hhplus.be.server.interfaces.api.user;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.interfaces.api.common.ApiResponse;
import kr.hhplus.be.server.interfaces.api.coupon.dto.CouponResponse;
import kr.hhplus.be.server.interfaces.api.user.dto.UserPointChargeRequest;
import kr.hhplus.be.server.interfaces.api.user.dto.UserPointResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Operation(summary = "잔액 조회 API", description = "유저의 보유 잔액을 반환한다.")
    @GetMapping("/{userId}/point")
    public ApiResponse<UserPointResponse> getUserPoint(@PathVariable Long userId) {
        UserPointResponse userPointResponse = UserPointResponse.builder()
                .id(1L)
                .point(10000)
                .build();

        return ApiResponse.ok(userPointResponse);
    }

    @Operation(summary = "잔액 충전 API", description = "유저의 보유 잔액을 충전하고 반환한다.")
    @PostMapping("/point/charge")
    public ApiResponse<UserPointResponse> chargeUserPoint(@RequestBody UserPointChargeRequest request) {
        UserPointResponse userPointResponse = UserPointResponse.builder()
                .id(1L)
                .point(20000)
                .build();

        return ApiResponse.ok(userPointResponse);
    }

    @Operation(summary = "보유 쿠폰 조회 API", description = "유저가 보유한 쿠폰 목록을 반환한다.")
    @PostMapping("/{userId}/coupons")
    public ApiResponse<List<CouponResponse>> getUserCoupons(@PathVariable Long userId) {

        CouponResponse coupon1 = CouponResponse.builder()
                .id(1L)
                .couponId(2L)
                .name("정률 할인 쿠폰")
                .discountType("PERCENTAGE")
                .discountAmt(10)
                .issuedAt(LocalDateTime.of(2025, 1, 1, 11, 0, 0))
                .validStartAt(LocalDateTime.of(2025, 1, 1, 11, 0, 0))
                .validEndAt(LocalDateTime.of(2025, 2, 1, 11, 0, 0))
                .usedAt(null)
                .status("UNUSED")
                .build();

        CouponResponse coupon2 = CouponResponse.builder()
                .id(1L)
                .couponId(3L)
                .name("정액 할인 쿠폰")
                .discountType("FIXED_AMOUNT")
                .discountAmt(4000)
                .issuedAt(LocalDateTime.of(2025, 1, 1, 11, 0, 0))
                .validStartAt(LocalDateTime.of(2025, 1, 1, 11, 0, 0))
                .validEndAt(LocalDateTime.of(2025, 2, 1, 11, 0, 0))
                .usedAt(null)
                .status("UNUSED")
                .build();

        return ApiResponse.ok(List.of(coupon1, coupon2));
    }
}
