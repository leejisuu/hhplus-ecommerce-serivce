package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.support.IntegrationTestSupport;
import kr.hhplus.be.server.application.payment.dto.result.PaymentResult;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PaymentApplicationServiceIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private PaymentApplicationService paymentApplicationService;

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
        Long issuedCouponId = 6L;
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 10, 0, 0);

        // when // then
        assertThatThrownBy(() -> paymentApplicationService.payment(userId, orderId, issuedCouponId, currentTime))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.ISSUED_COUPON_NOT_FOUND.getMessage());
    }

    @Test
    void 정률_쿠폰_사용_시_쿠폰의_할인_금액을_계산해서_결제를_생성한다() {
        // given
        Long userId = 3L;
        Long orderId = 1L;
        Long issuedCouponId = 2L;
        BigDecimal totalOriginalAmt = setScaleFromInt(31400);
        BigDecimal discountAmt = (totalOriginalAmt.multiply(new BigDecimal(10))).divide(new BigDecimal(100));
        BigDecimal finalPaymentAmt = totalOriginalAmt.subtract(discountAmt);
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 10, 0, 0);

        // when
        PaymentResult.Payment payment = paymentApplicationService.payment(userId, orderId, issuedCouponId, currentTime);

        // then
        assertThat(payment.orderId()).isEqualTo(orderId);
        assertThat(payment.totalOriginalAmt().compareTo(totalOriginalAmt)).isEqualTo(0);
        assertThat(payment.discountAmt().compareTo(discountAmt)).isEqualTo(0);
        assertThat(payment.finalPaymentAmt().compareTo(finalPaymentAmt)).isEqualTo(0);
    }

    @Test
    void 정액_쿠폰_사용_시_쿠폰의_할인_금액을_계산해서_결제를_생성한다() {
        // given
        Long userId = 3L;
        Long orderId = 1L;
        Long issuedCouponId = 5L;
        BigDecimal totalOriginalAmt = setScaleFromInt(31400);
        BigDecimal discountAmt = setScaleFromInt(5000);
        BigDecimal finalPaymentAmt = totalOriginalAmt.subtract(discountAmt);
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 10, 0, 0);

        // when
        PaymentResult.Payment payment = paymentApplicationService.payment(userId, orderId, issuedCouponId, currentTime);

        // then
        assertThat(payment.orderId()).isEqualTo(orderId);
        assertThat(payment.totalOriginalAmt().compareTo(totalOriginalAmt)).isEqualTo(0);
        assertThat(payment.discountAmt().compareTo(discountAmt)).isEqualTo(0);
        assertThat(payment.finalPaymentAmt().compareTo(finalPaymentAmt)).isEqualTo(0);
    }

    @Test
    void 결제_시_포인트가_부족하면_CustomException_INSUFFICIENT_POINT_예외를_발생한다() {
        // given
        Long userId = 1L;
        Long orderId = 1L;
        Long issuedCouponId = null;
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 10, 0, 0);

        // when // then
        assertThatThrownBy(() -> paymentApplicationService.payment(userId, orderId, issuedCouponId, currentTime))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INSUFFICIENT_POINT.getMessage());
    }

    private static BigDecimal setScaleFromInt(int value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
    }
}
