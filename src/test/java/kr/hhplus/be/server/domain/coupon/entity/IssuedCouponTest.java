package kr.hhplus.be.server.domain.coupon.entity;

import kr.hhplus.be.server.domain.coupon.enums.DiscountType;
import kr.hhplus.be.server.domain.coupon.enums.IssuedCouponStatus;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.support.exception.CustomException;
import kr.hhplus.be.server.support.exception.ErrorCode;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class IssuedCouponTest {

    @Test
    void 정액_타입_쿠폰_할인_금액을_계신한다() {
        // Given
        IssuedCoupon issuedCoupon= IssuedCoupon.builder()
                .user(new User("유저", 1000)) // 테스트용 User 객체, 필요 시 테스트 전용 생성자 또는 Mock 사용
                .coupon(new Coupon()) // 테스트용 Coupon 객체
                .name("쿠폰")
                .discountType(DiscountType.FIXED_AMOUNT) // 또는 FIXED
                .discountAmt(500) // 고정 금액 할인
                .issuedAt(LocalDateTime.now().minusDays(1)) // 발급 시간
                .validStartedAt(LocalDateTime.now().minusDays(1)) // 유효 시작 시간
                .validEndedAt(LocalDateTime.now().plusDays(1)) // 유효 종료 시간
                .usedAt(null) // 사용 시간 초기값
                .status(IssuedCouponStatus.UNUSED) // 기본 상태
                .build();

        int netAmt = 1000; // 순수 금액

        // When
        int discountAmt = issuedCoupon.calculateDiscountAmt(netAmt);

        // Then
        assertThat(discountAmt).isEqualTo(netAmt - issuedCoupon.getDiscountAmt());
    }

    @Test
    void 정률_타입_쿠폰_할인_금액을_계신한다() {
        // Given
        IssuedCoupon issuedCoupon= IssuedCoupon.builder()
                .user(new User("유저", 1000)) // 테스트용 User 객체, 필요 시 테스트 전용 생성자 또는 Mock 사용
                .coupon(new Coupon()) // 테스트용 Coupon 객체
                .name("쿠폰")
                .discountType(DiscountType.PERCENTAGE) // 또는 FIXED
                .discountAmt(10) // 10% 할인
                .issuedAt(LocalDateTime.now().minusDays(1)) // 발급 시간
                .validStartedAt(LocalDateTime.now().minusDays(1)) // 유효 시작 시간
                .validEndedAt(LocalDateTime.now().plusDays(1)) // 유효 종료 시간
                .usedAt(null) // 사용 시간 초기값
                .status(IssuedCouponStatus.UNUSED) // 기본 상태
                .build();


        int netAmt = 1000; // 순수 금액

        // When
        int discountAmt = issuedCoupon.calculateDiscountAmt(netAmt);

        // Then
        assertThat(discountAmt).isEqualTo(netAmt * issuedCoupon.getDiscountAmt() / 100);
    }

    @Test
    void 쿠폰_할인_금액이_순수_금액을_초과하면_예외를_발생한다() {
        // Given
        IssuedCoupon issuedCoupon= IssuedCoupon.builder()
                .user(new User("유저", 1000)) // 테스트용 User 객체, 필요 시 테스트 전용 생성자 또는 Mock 사용
                .coupon(new Coupon()) // 테스트용 Coupon 객체
                .name("쿠폰")
                .discountType(DiscountType.FIXED_AMOUNT) // 또는 FIXED
                .discountAmt(1000) // 고정 금액 할인
                .issuedAt(LocalDateTime.now().minusDays(1)) // 발급 시간
                .validStartedAt(LocalDateTime.now().minusDays(1)) // 유효 시작 시간
                .validEndedAt(LocalDateTime.now().plusDays(1)) // 유효 종료 시간
                .usedAt(null) // 사용 시간 초기값
                .status(IssuedCouponStatus.UNUSED) // 기본 상태
                .build();

        int netAmt = 1000; // 순수 금액

        // when // then
        assertThatThrownBy(() -> issuedCoupon.calculateDiscountAmt(netAmt))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.COUPON_DISCOUNT_EXCEEDS_NET_AMOUNT.getMessage());
    }

    @Test
    void 쿠폰을_사용_메서드를_호출하면_쿠폰상태가_USED로_변경되고_usedAt이_null이_아니다() {
        // Given
        IssuedCoupon issuedCoupon= IssuedCoupon.builder()
                .user(new User("유저", 1000)) // 테스트용 User 객체, 필요 시 테스트 전용 생성자 또는 Mock 사용
                .coupon(new Coupon()) // 테스트용 Coupon 객체
                .name("쿠폰")
                .discountType(DiscountType.FIXED_AMOUNT) // 또는 FIXED
                .discountAmt(1000) // 고정 금액 할인
                .issuedAt(LocalDateTime.now().minusDays(1)) // 발급 시간
                .validStartedAt(LocalDateTime.now().minusDays(1)) // 유효 시작 시간
                .validEndedAt(LocalDateTime.now().plusDays(1)) // 유효 종료 시간
                .usedAt(null) // 사용 시간 초기값
                .status(IssuedCouponStatus.UNUSED) // 기본 상태
                .build();

        // when
        issuedCoupon.useCoupon();

        // then
        assertThat(issuedCoupon.getStatus()).isEqualTo(IssuedCouponStatus.USED);
        assertThat(issuedCoupon.getUsedAt()).isNotNull();
    }

}