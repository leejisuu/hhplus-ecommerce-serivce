package kr.hhplus.be.server.domain.payment.entity;

import kr.hhplus.be.server.domain.payment.enums.PaymentStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class PaymentTest {

    @Test
    void 결제_생성_시_IN_PROGRESS인_상태를_결제_완료_후_COMPLETED로_변경한다() {
        // given
        Payment payment = Payment.create(1L, new BigDecimal(10000), new BigDecimal(2000),1L);

        // when
        payment.complete();

        // then
        Assertions.assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
    }

    @Test
    void 결제_생성_시_IN_PROGRESS인_상태를_결제_실패_후_FAILED로_변경한다() {
        // given
        Payment payment = Payment.create(1L, new BigDecimal(10000), new BigDecimal(2000),1L);

        // when
        payment.fail();

        // then
        Assertions.assertThat(payment.getStatus()).isEqualTo(PaymentStatus.FAILED);
    }
}
