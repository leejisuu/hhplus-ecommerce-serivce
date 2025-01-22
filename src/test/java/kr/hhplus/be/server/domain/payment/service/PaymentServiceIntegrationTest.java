package kr.hhplus.be.server.domain.payment.service;

import kr.hhplus.be.server.IntegrationTestSupport;
import kr.hhplus.be.server.domain.payment.dto.info.PaymentInfo;
import kr.hhplus.be.server.domain.payment.enums.PaymentStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class PaymentServiceIntegrationTest extends IntegrationTestSupport {

    @Autowired
    private PaymentService paymentService;

    @Test
    void 할인쿠폰_없이_결제_생성_시_할인금액을_0원으로_하여_결제를_생성한다() {
        // given
        Long orderId = 1L;
        BigDecimal totalOriginalAmt = new BigDecimal(31400);
        BigDecimal discountAmt = BigDecimal.ZERO;
        BigDecimal finalPaymentAmt = totalOriginalAmt.subtract(discountAmt);
        Long issuedCouponId = null;

        // when
        PaymentInfo.PaymentDto payment = paymentService.payment(orderId, totalOriginalAmt, discountAmt, issuedCouponId);

        // then
        Assertions.assertThat(payment)
                .extracting("orderId", "status", "totalOriginalAmt", "discountAmt", "finalPaymentAmt")
                .containsExactly(orderId, PaymentStatus.COMPLETED.name(), totalOriginalAmt, discountAmt, finalPaymentAmt);
    }

    @Test
    void 정액_할인_쿠폰을_사용해서_할인_금액을_계산하여_결제를_생성한다() {
        // given
        Long orderId = 1L;
        BigDecimal totalOriginalAmt = new BigDecimal(31400);
        BigDecimal discountAmt = new BigDecimal(3000);
        BigDecimal finalPaymentAmt = totalOriginalAmt.subtract(discountAmt);
        Long issuedCouponId = 1L;

        // when
        PaymentInfo.PaymentDto payment = paymentService.payment(orderId, totalOriginalAmt, discountAmt, issuedCouponId);

        // then
        Assertions.assertThat(payment)
                .extracting("orderId", "status", "totalOriginalAmt", "discountAmt", "finalPaymentAmt")
                .containsExactly(orderId, PaymentStatus.COMPLETED.name(), totalOriginalAmt, discountAmt, finalPaymentAmt);
    }

    @Test
    void 정률_할인_쿠폰을_사용해서_할인_금액을_계산하여_결제를_생성한다() {
        // given
        Long orderId = 1L;
        BigDecimal totalOriginalAmt = new BigDecimal(31400);
        BigDecimal discountAmt = new BigDecimal(3140);
        BigDecimal finalPaymentAmt = totalOriginalAmt.subtract(discountAmt);
        Long issuedCouponId = 2L;

        // when
        PaymentInfo.PaymentDto payment = paymentService.payment(orderId, totalOriginalAmt, discountAmt, issuedCouponId);

        // then
        Assertions.assertThat(payment)
                .extracting("orderId", "status", "totalOriginalAmt", "discountAmt", "finalPaymentAmt")
                .containsExactly(orderId, PaymentStatus.COMPLETED.name(), totalOriginalAmt, discountAmt, finalPaymentAmt);
    }
}
