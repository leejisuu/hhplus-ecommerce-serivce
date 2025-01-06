package kr.hhplus.be.server.interfaces.api.payment;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.interfaces.api.common.ApiResponse;
import kr.hhplus.be.server.interfaces.api.payment.dto.PaymentMakeRequest;
import kr.hhplus.be.server.interfaces.api.payment.dto.PaymentMakeResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Operation(summary = "결제 API", description = "결제한다.")
    @PostMapping("/make")
    public ApiResponse<PaymentMakeResponse> makePayment(@RequestBody PaymentMakeRequest request) {
        PaymentMakeResponse response = PaymentMakeResponse.builder()
                .id(1L)
                .orderId(4L)
                .status("COMPLETED")
                .totalAmt(67000)
                .createdAt(LocalDateTime.of(2024, 1, 3, 11, 0))
                .build();

        return ApiResponse.ok(response);
    }
}
