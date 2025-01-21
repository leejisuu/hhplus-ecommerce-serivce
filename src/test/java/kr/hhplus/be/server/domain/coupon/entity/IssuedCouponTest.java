
package kr.hhplus.be.server.domain.coupon.entity;

import kr.hhplus.be.server.domain.coupon.enums.DiscountType;
import kr.hhplus.be.server.domain.coupon.enums.IssuedCouponStatus;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class IssuedCouponTest {

    @Test
    void 정액_타입_쿠폰_할인_금액을_계신한다() {
        Long userId = 1L;
        Long couponId = 1L;
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 20, 15, 0, 0);
        LocalDateTime issuedAt = LocalDateTime.of(2025, 1, 19, 15, 0, 0);
        LocalDateTime validStartedAt = LocalDateTime.of(2025, 1, 1, 15, 0, 0);
        LocalDateTime validEndedAt = LocalDateTime.of(2025, 1, 31, 15, 0, 0);

        // Given
        IssuedCoupon issuedCoupon= IssuedCoupon.builder()
                .userId(userId)
                .couponId(couponId)
                .name("쿠폰")
                .discountType(DiscountType.FIXED_AMOUNT)
                .discountAmt(new BigDecimal(5000))
                .issuedAt(issuedAt)
                .validStartedAt(validStartedAt)
                .validEndedAt(validEndedAt)
                .usedAt(null)
                .status(IssuedCouponStatus.UNUSED)
                .build();

        BigDecimal totalOriginalAmt = new BigDecimal(10000);

        // When
        BigDecimal discountAmt = issuedCoupon.use(totalOriginalAmt, currentTime);

        // Then
        assertThat(discountAmt).isEqualTo(issuedCoupon.getDiscountAmt());
        assertThat(issuedCoupon.getStatus()).isEqualTo(IssuedCouponStatus.USED);
        assertThat(issuedCoupon.getUsedAt()).isEqualTo(currentTime);
    }

    @Test
    void 정률_타입_쿠폰_할인_금액을_계신한다() {
        // Given
        Long userId = 1L;
        Long couponId = 1L;
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 20, 15, 0, 0);
        LocalDateTime issuedAt = LocalDateTime.of(2025, 1, 19, 15, 0, 0);
        LocalDateTime validStartedAt = LocalDateTime.of(2025, 1, 1, 15, 0, 0);
        LocalDateTime validEndedAt = LocalDateTime.of(2025, 1, 31, 15, 0, 0);

        // Given
        IssuedCoupon issuedCoupon= IssuedCoupon.builder()
                .userId(userId)
                .couponId(couponId)
                .name("쿠폰")
                .discountType(DiscountType.PERCENTAGE)
                .discountAmt(new BigDecimal(10))
                .issuedAt(issuedAt)
                .validStartedAt(validStartedAt)
                .validEndedAt(validEndedAt)
                .usedAt(null)
                .status(IssuedCouponStatus.UNUSED)
                .build();


        BigDecimal totalOriginalAmt = new BigDecimal(10000);

        // When
        BigDecimal discountAmt = issuedCoupon.use(totalOriginalAmt, currentTime);

        // Then
        assertThat(discountAmt).isEqualTo((totalOriginalAmt.multiply(issuedCoupon.getDiscountAmt())).divide(new BigDecimal(100)));
        assertThat(issuedCoupon.getStatus()).isEqualTo(IssuedCouponStatus.USED);
        assertThat(issuedCoupon.getUsedAt()).isEqualTo(currentTime);
    }

    @Test
    void 쿠폰_할인_금액이_순수_금액_이상이면_예외를_발생한다() {
        // Given
        Long userId = 1L;
        Long couponId = 1L;
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 20, 15, 0, 0);
        LocalDateTime issuedAt = LocalDateTime.of(2025, 1, 19, 15, 0, 0);
        LocalDateTime validStartedAt = LocalDateTime.of(2025, 1, 1, 15, 0, 0);
        LocalDateTime validEndedAt = LocalDateTime.of(2025, 1, 31, 15, 0, 0);

        // Given
        IssuedCoupon issuedCoupon= IssuedCoupon.builder()
                .userId(userId)
                .couponId(couponId)
                .name("쿠폰")
                .discountType(DiscountType.FIXED_AMOUNT)
                .discountAmt(new BigDecimal(1000))
                .issuedAt(issuedAt)
                .validStartedAt(validStartedAt)
                .validEndedAt(validEndedAt)
                .usedAt(null)
                .status(IssuedCouponStatus.UNUSED)
                .build();

        BigDecimal totalOriginalAmt = new BigDecimal(1000);

        // when // then
        assertThatThrownBy(() -> issuedCoupon.use(totalOriginalAmt, currentTime))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.COUPON_DISCOUNT_EXCEEDS_NET_AMOUNT.getMessage());
    }

    @Test
    void 쿠폰_사용_메서드를_호출하면_쿠폰상태가_USED로_변경되고_usedAt이_현재_시간이_된다() {
        // Given
        Long userId = 1L;
        Long couponId = 1L;
        LocalDateTime currentTime = LocalDateTime.of(2025, 1, 20, 15, 0, 0);
        LocalDateTime issuedAt = LocalDateTime.of(2025, 1, 19, 15, 0, 0);
        LocalDateTime validStartedAt = LocalDateTime.of(2025, 1, 1, 15, 0, 0);
        LocalDateTime validEndedAt = LocalDateTime.of(2025, 1, 31, 15, 0, 0);

        // Given
        IssuedCoupon issuedCoupon= IssuedCoupon.builder()
                .userId(userId)
                .couponId(couponId)
                .name("쿠폰")
                .discountType(DiscountType.FIXED_AMOUNT)
                .discountAmt(new BigDecimal(5000))
                .issuedAt(issuedAt)
                .validStartedAt(validStartedAt)
                .validEndedAt(validEndedAt)
                .usedAt(null)
                .status(IssuedCouponStatus.UNUSED)
                .build();

        BigDecimal totalOriginalAmt = new BigDecimal(10000);

        // When
        issuedCoupon.use(totalOriginalAmt, currentTime);

        // Then
        assertThat(issuedCoupon.getStatus()).isEqualTo(IssuedCouponStatus.USED);
        assertThat(issuedCoupon.getUsedAt()).isEqualTo(currentTime);
    }

}
