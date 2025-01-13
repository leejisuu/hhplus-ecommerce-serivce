package kr.hhplus.be.server.interfaces.api.user;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.application.user.UserApplicationService;
import kr.hhplus.be.server.domain.user.service.UserService;
import kr.hhplus.be.server.interfaces.api.common.ApiResponse;
import kr.hhplus.be.server.interfaces.api.user.dto.UserPointChargeRequest;
import kr.hhplus.be.server.interfaces.api.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserApplicationService userApplicationService;

    @Operation(summary = "잔액 조회 API", description = "유저의 보유 잔액을 반환한다.")
    @GetMapping("/{userId}/point")
    public ApiResponse<UserResponse> getUserPoint(@PathVariable Long userId) {
        return ApiResponse.ok(UserResponse.from(userService.getUser(userId)));
    }

    @Operation(summary = "잔액 충전 API", description = "유저의 보유 잔액을 충전하고 반환한다.")
    @PostMapping("/point/charge")
    public ApiResponse<UserResponse> chargeUserPoint(@RequestBody UserPointChargeRequest request) {
        return ApiResponse.ok(UserResponse.from(userApplicationService.chargeUserPoint(request.userId(), request.amount())));
    }
}
