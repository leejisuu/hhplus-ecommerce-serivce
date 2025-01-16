package kr.hhplus.be.server.interfaces.api.point;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.domain.point.service.PointService;
import kr.hhplus.be.server.interfaces.api.common.ApiResponse;
import kr.hhplus.be.server.interfaces.api.point.dto.request.PointRequest;
import kr.hhplus.be.server.interfaces.api.point.dto.response.PointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/points")
public class PointController {

    private final PointService pointService;

    @Operation(summary = "잔액 조회 API", description = "유저의 보유 잔액을 반환한다.")
    @GetMapping("/{userId}")
    public ApiResponse<PointResponse.Point> getUserPoint(@PathVariable Long userId) {
        return ApiResponse.ok(PointResponse.Point.of(pointService.getPoint(userId)));
    }

    @Operation(summary = "잔액 충전 API", description = "유저의 보유 잔액을 충전하고 반환한다.")
    @PostMapping("/charge")
    public ApiResponse<PointResponse.Point> chargeUserPoint(@RequestBody PointRequest.Charge request) {
        return ApiResponse.ok(PointResponse.Point.of(pointService.charge(request.userId(), request.amount())));
    }
}
