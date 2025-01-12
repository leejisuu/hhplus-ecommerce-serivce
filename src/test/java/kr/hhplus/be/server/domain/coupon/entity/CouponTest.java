package kr.hhplus.be.server.domain.coupon.entity;

import kr.hhplus.be.server.domain.coupon.enums.CouponStatus;
import kr.hhplus.be.server.domain.coupon.enums.DiscountType;
import kr.hhplus.be.server.domain.coupon.enums.IssuedCouponStatus;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.support.exception.CustomException;
import kr.hhplus.be.server.support.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CouponTest {

    User user;

    @BeforeEach
    void setUp() {
        user = new User("김항해", 1000);
    }

    @Test
    void 쿠폰의_상태가_DEACTIVATEDE면_CustomException_예외를_발생한다() {
        // given
        LocalDateTime issuedAt = LocalDateTime.of(2025, 1, 2, 10, 0, 0);
        LocalDateTime validStartedAt = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        LocalDateTime validEndedAt = LocalDateTime.of(2025, 1, 3, 10, 0, 0);
        Coupon coupon = new Coupon("쿠폰", DiscountType.FIXED_AMOUNT, 1000, 30, 30, validStartedAt, validEndedAt, CouponStatus.DEACTIVATED);

        // when // then
        assertThatThrownBy(() -> coupon.makeIssuedCoupon(user, issuedAt))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.DEACTIVATED_COUPON.getMessage());
    }

    @Test
    void 쿠폰의_유효기간이_만료됐다면_CustomException_예외를_발생한다() {
        // given
        LocalDateTime issuedAt = LocalDateTime.of(2025, 1, 3, 10, 0, 0);
        LocalDateTime validStartedAt = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        LocalDateTime validEndedAt = LocalDateTime.of(2025, 1, 3, 10, 0, 0);
        Coupon coupon = new Coupon("쿠폰", DiscountType.FIXED_AMOUNT, 1000, 30, 30, validStartedAt, validEndedAt, CouponStatus.ACTIVE);

        // when // then
        Assertions.assertThatThrownBy(() -> coupon.makeIssuedCoupon(user, issuedAt))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.COUPON_EXPIRED.getMessage());
    }

    @Test
    void 쿠폰의_잔여개수가_0개_이하라면_CustomException_예외를_발생한다() {
        // given
        LocalDateTime issuedAt = LocalDateTime.of(2025, 1, 2, 10, 0, 0);
        LocalDateTime validStartedAt = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        LocalDateTime validEndedAt = LocalDateTime.of(2025, 1, 3, 10, 0, 0);
        Coupon coupon = new Coupon("쿠폰", DiscountType.FIXED_AMOUNT, 1000, 30, 0, validStartedAt, validEndedAt, CouponStatus.ACTIVE);

        // when // then
        Assertions.assertThatThrownBy(() -> coupon.makeIssuedCoupon(user, issuedAt))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INSUFFICIENT_COUPON_QUANTITY.getMessage());
    }

    @Test
    void 쿠폰의_잔여개수가_1개_이상이라면_잔여개수를_1개_차감한다() {
        // given
        LocalDateTime issuedAt = LocalDateTime.of(2025, 1, 2, 10, 0, 0);
        LocalDateTime validStartedAt = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        LocalDateTime validEndedAt = LocalDateTime.of(2025, 1, 3, 10, 0, 0);
        Coupon coupon = new Coupon("쿠폰", DiscountType.FIXED_AMOUNT, 1000, 30, 1, validStartedAt, validEndedAt, CouponStatus.ACTIVE);

        // when
        coupon.makeIssuedCoupon(user, issuedAt);

        // then
        assertThat(coupon.getRemainCapacity()).isEqualTo(0);
    }

    @Test
    void 쿠폰_마스터_정보를_기반으로_발급_받을_쿠폰_엔티티를_반환한다() {
        // given
        LocalDateTime issuedAt = LocalDateTime.of(2025, 1, 2, 10, 0, 0);
        LocalDateTime validStartedAt = LocalDateTime.of(2025, 1, 1, 10, 0, 0);
        LocalDateTime validEndedAt = LocalDateTime.of(2025, 1, 3, 10, 0, 0);
        Coupon coupon = new Coupon("쿠폰", DiscountType.FIXED_AMOUNT, 1000, 30, 1, validStartedAt, validEndedAt, CouponStatus.ACTIVE);

        // when
        IssuedCoupon issuedCoupon = coupon.makeIssuedCoupon(user, issuedAt);

        // then
        assertThat(issuedCoupon)
                .extracting("user", "coupon", "name", "discountType", "discountAmt", "issuedAt", "validStartedAt", "validEndedAt", "usedAt", "status")
                .containsExactly(user, coupon, coupon.getName(), coupon.getDiscountType(), coupon.getDiscountAmt(), issuedAt, coupon.getValidStartedAt(), coupon.getValidEndedAt(), null, IssuedCouponStatus.UNUSED);
    }
}