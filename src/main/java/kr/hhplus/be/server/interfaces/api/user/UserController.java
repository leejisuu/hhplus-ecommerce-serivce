package kr.hhplus.be.server.interfaces.api.user;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.interfaces.api.common.ApiResponse;
import kr.hhplus.be.server.interfaces.api.user.dto.UserCouponResponse;
import kr.hhplus.be.server.interfaces.api.user.dto.UserCouponsResponse;
import kr.hhplus.be.server.interfaces.api.user.dto.UserPointRequest;
import kr.hhplus.be.server.interfaces.api.user.dto.UserPointResponse;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Operation(summary = "Get user point", description = "유저의 보유 잔액을 반환한다..")
    @GetMapping("/point/{userId}")
    public ApiResponse<UserPointResponse> getPoint(@PathVariable Long userId) {
        UserPointResponse userPointResponse = UserPointResponse.builder()
                .id(1L)
                .point(10000)
                .build();

        return ApiResponse.ok(userPointResponse);
    }

    @Operation(summary = "Charge user point", description = "유저의 보유 잔액을 충전하고 반환한다.")
    @PostMapping("/point/charge")
    public ApiResponse<UserPointResponse> chargePoint(@RequestBody UserPointRequest request) {
        UserPointResponse userPointResponse = UserPointResponse.builder()
                .id(1L)
                .point(20000)
                .build();

        return ApiResponse.ok(userPointResponse);
    }

    @Operation(summary = "Get user coupons", description = "유저가 보유한 쿠폰 목록을 반환한다.")
    @PostMapping("/coupons")
    public ApiResponse<UserCouponsResponse> getUserCoupons(@PathVariable Long userId) {

        UserCouponResponse coupon1 = UserCouponResponse.builder()
                .id(1L)
                .couponId(2L)
                .couponName("정률 할인 쿠폰")
                .discountType("PERCENTAGE")
                .discountAmt(10)
                .validStartAt(LocalDate.of(2025, 1, 1))
                .validEndAt(LocalDate.of(2025, 2, 1))
                .status("NOT USED")
                .build();

        UserCouponResponse coupon2 = UserCouponResponse.builder()
                .id(1L)
                .couponId(3L)
                .couponName("정액 할인 쿠폰")
                .discountType("FIXED_AMOUNT")
                .discountAmt(4000)
                .validStartAt(LocalDate.of(2025, 1, 1))
                .validEndAt(LocalDate.of(2025, 3, 1))
                .status("NOT USED")
                .build();

        UserCouponsResponse userCouponsResponse = UserCouponsResponse.builder()
                .id(1L)
                .userCoupons(List.of(coupon1, coupon2))
                .build();

        return ApiResponse.ok(userCouponsResponse);
    }
}
