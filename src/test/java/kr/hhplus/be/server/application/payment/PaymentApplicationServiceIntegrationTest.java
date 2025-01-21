package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.IntegrationTestSupport;
import kr.hhplus.be.server.application.payment.dto.result.PaymentResult;
import kr.hhplus.be.server.domain.coupon.service.CouponService;
import kr.hhplus.be.server.domain.order.service.OrderService;
import kr.hhplus.be.server.domain.payment.enums.PaymentStatus;
import kr.hhplus.be.server.domain.payment.service.PaymentService;
import kr.hhplus.be.server.domain.point.service.PointService;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PaymentApplicationServiceIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private PaymentApplicationService paymentApplicationService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PointService pointService;

    @Autowired
    private CouponService couponService;

    @Test
    void 주문_정보가_없으면_CustomException_ORDER_NOT_FOUND_예외를_발생한다() {
        // given
        Long userId = 3L;
        Long orderId = 3L;
        Long issuedCouponId = null;
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 10, 0, 0);

        // when // then
        assertThatThrownBy(() -> paymentApplicationService.payment(userId, orderId, issuedCouponId, currentTime))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ORDER_NOT_FOUND.getMessage());
    }

    @Test
    void 쿠폰_정보가_없으면_CustomException_ISSUED_COUPON_NOT_FOUND_예외를_발생한다() {
        // given
        Long userId = 1L;
        Long orderId = 1L;
        Long issuedCouponId = 5L;
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 10, 0, 0);

        // when // then
        assertThatThrownBy(() -> paymentApplicationService.payment(userId, orderId, issuedCouponId, currentTime))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ISSUED_COUPON_NOT_FOUND.getMessage());
    }

    /*@Test
    void 정률_쿠폰_사용_시_쿠폰의_할인_금액을_계산해서_결제를_생성한다() {
        // given
        Long userId = 3L;
        Long orderId = 1L;
        Long issuedCouponId = 2L;
        BigDecimal totalOriginalAmt = new BigDecimal(31400);
        BigDecimal discountAmt = (totalOriginalAmt.multiply(new BigDecimal(10))).divide(new BigDecimal(100));
        BigDecimal finalPaymentAmt = totalOriginalAmt.subtract(discountAmt);
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 10, 0, 0);

        // when
        PaymentResult.Payment payment = paymentApplicationService.payment(userId, orderId, issuedCouponId, currentTime);

        // then
        assertThat(payment)
                .extracting("orderId", "totalOriginalAmt", "discountAmt", "finalPaymentAmt")
                .containsExactly(orderId, totalOriginalAmt, discountAmt, finalPaymentAmt);
    }

    @Test
    void 정액_쿠폰_사용_시_쿠폰의_할인_금액을_계산해서_결제를_생성한다() {
        // given
        Long userId = 3L;
        Long orderId = 2L;
        Long issuedCouponId = 5L;
        BigDecimal totalOriginalAmt = new BigDecimal(31400);
        BigDecimal discountAmt = new BigDecimal(3000);
        BigDecimal finalPaymentAmt = totalOriginalAmt.subtract(discountAmt);
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 10, 0, 0);

        // when
        PaymentResult.Payment payment = paymentApplicationService.payment(userId, orderId, issuedCouponId, currentTime);

        // then
        assertThat(payment)
                .extracting("orderId", "totalOriginalAmt", "discountAmt", "finalPaymentAmt")
                .containsExactly(orderId, totalOriginalAmt, discountAmt, finalPaymentAmt);
    }

    @Test
    void 결제_시_포인트가_부족하면_결제_상태를_FAILD로_변경한다() {
        // given
        Long userId = 1L;
        Long orderId = 1L;
        Long issuedCouponId = null;
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 10, 0, 0);

        // when
        PaymentResult.Payment payment = paymentApplicationService.payment(userId, orderId, issuedCouponId, currentTime);

        // then
        assertThat(payment.status()).isEqualTo(PaymentStatus.FAILED.name());
    }*/

    @Test
    void 결제_완료_후_결제의_상태를_COMPLETED로_변경한다() {
        Long userId = 3L;
        Long orderId = 2L;
        Long issuedCouponId = null;
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 10, 0, 0);

        // when
        PaymentResult.Payment payment = paymentApplicationService.payment(userId, orderId, issuedCouponId, currentTime);

        // then
        assertThat(payment.status()).isEqualTo(PaymentStatus.COMPLETED.name());
    }
}
