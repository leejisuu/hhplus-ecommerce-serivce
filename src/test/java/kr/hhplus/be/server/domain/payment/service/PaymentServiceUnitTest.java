package kr.hhplus.be.server.domain.payment.service;

import kr.hhplus.be.server.domain.payment.dto.info.PaymentInfo;
import kr.hhplus.be.server.domain.payment.entity.Payment;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceUnitTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Test
    void 결제를_생성한다() {
        // given
        Long orderId = 1L;
        BigDecimal totalOriginalAmt = new BigDecimal(10000);
        BigDecimal discountAmt = new BigDecimal(2000);
        Long issuedCouponId = 1L;

        Payment mockPayment = Payment.create(orderId, totalOriginalAmt, discountAmt, issuedCouponId);

        given(paymentRepository.save(any(Payment.class))).willReturn(mockPayment);

        // when
        PaymentInfo.PaymentDto payment =  paymentService.payment(orderId, totalOriginalAmt, discountAmt, issuedCouponId);

        // then
        assertThat(payment)
                .extracting("orderId", "status", "totalOriginalAmt", "discountAmt", "finalPaymentAmt")
                .containsExactly(orderId, mockPayment.getStatus().name(), mockPayment.getTotalOriginalAmt(), mockPayment.getDiscountAmt(), mockPayment.getFinalPaymentAmt());

        verify(paymentRepository, times(1)).save(any(Payment.class));
    }
}
