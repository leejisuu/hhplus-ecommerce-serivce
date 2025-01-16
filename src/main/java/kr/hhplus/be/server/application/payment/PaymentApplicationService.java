package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.payment.dto.result.PaymentResult;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import kr.hhplus.be.server.domain.order.dto.info.OrderInfo;
import kr.hhplus.be.server.domain.order.service.OrderService;
import kr.hhplus.be.server.domain.payment.dto.info.PaymentInfo;
import kr.hhplus.be.server.domain.payment.service.PaymentService;
import kr.hhplus.be.server.domain.point.service.PointService;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class PaymentApplicationService {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final PointService pointService;
    private final CouponService couponService;

    @Transactional
    public PaymentResult.Payment payment(Long userId, Long orderId, Long issuedCouponId, LocalDateTime currentTime) {
        OrderInfo.OrderDto order = orderService.getOrder(orderId);
        BigDecimal discountAmt = BigDecimal.ZERO;
        if(issuedCouponId != null) {
            discountAmt = couponService.useIssuedCoupon(issuedCouponId, order.totalOriginalAmt(), currentTime);
        }
        PaymentInfo.PaymentDto payment = paymentService.payment(orderId, order.totalOriginalAmt(), discountAmt, issuedCouponId);

        try {
            pointService.use(userId, payment.finalPaymentAmt());
            paymentService.failPayment(payment.id());
        } catch (CustomException e) {
            paymentService.completePayment(payment.id());
        }

        return PaymentResult.Payment.of(payment);
    }
}
