package kr.hhplus.be.server.interfaces.api.payment;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.application.payment.PaymentApplicationService;
import kr.hhplus.be.server.interfaces.api.common.ApiResponse;
import kr.hhplus.be.server.interfaces.api.payment.dto.PaymentRequest;
import kr.hhplus.be.server.interfaces.api.payment.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentApplicationService paymentApplicationService;

    @Operation(summary = "결제 API", description = "결제한다.")
    @PostMapping("/make")
    public ApiResponse<PaymentResponse.Payment> payment(@RequestBody PaymentRequest.Payment request) {
        LocalDateTime currentTime = LocalDateTime.now();
        return ApiResponse.ok(PaymentResponse.Payment.of(paymentApplicationService.payment(request.userId(), request.orderId(), request.issuedCouponId(), currentTime)));
    }
}
