package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.dto.info.IssuedCouponInfo;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.enums.CouponStatus;
import kr.hhplus.be.server.domain.coupon.enums.DiscountType;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.repository.IssuedCouponRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class CouponUnitServiceTest {

    @InjectMocks
    private CouponService couponService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private IssuedCouponRepository issuedCouponRepository;

    @Nested
    @DisplayName("쿠폰 발급 단위 테스트")
    class IssueCouponTest {
        @Test
        void 쿠폰_발급_시_쿠폰_마스터_정보가_없으면_예외가_발생한다() {
            // given
            Long couponId = 1L;
            Long userId = 1L;
            LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 1 ,0 ,0);

            given(couponRepository.getCoupon(couponId)).willReturn(null);

            // when // then
            assertThatThrownBy(() -> couponService.issue(couponId, userId, currentTime))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.COUPON_NOT_FOUND.getMessage());
        }

        @Test
        void 쿠폰_발급_시_이미_발급_받았다면_예외를_발생한다() {
            // given
            Long couponId = 1L;
            Long userId = 1L;
            LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 1 ,0 ,0);

            Coupon coupon = Coupon.create("생일 쿠폰", DiscountType.FIXED_AMOUNT, new BigDecimal(3000), 3, 3,
                    LocalDateTime.of(2025, 1, 1, 0 ,0 ,0), LocalDateTime.of(2025, 1, 31, 23 ,59 ,59), CouponStatus.ACTIVE);
            IssuedCoupon issuedCoupon = coupon.issue(userId, currentTime);

            given(couponRepository.getCoupon(couponId)).willReturn(coupon);
            given(issuedCouponRepository.findByCouponIdAndUserId(couponId, userId)).willReturn(issuedCoupon);

            // when // then
            assertThatThrownBy(() -> couponService.issue(couponId, userId, currentTime))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.ALREADY_ISSUED_COUPON.getMessage());
        }

        @Test
        void 쿠폰을_발급_받는다() {
            // given
            Long couponId = 1L;
            Long userId = 1L;
            LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 1 ,0 ,0);

            Coupon mockCoupon = Coupon.create("생일 쿠폰", DiscountType.FIXED_AMOUNT, new BigDecimal(3000), 3, 3,
                    LocalDateTime.of(2025, 1, 1, 0 ,0 ,0), LocalDateTime.of(2025, 1, 31, 23 ,59 ,59), CouponStatus.ACTIVE);
            IssuedCoupon mockIssuedCoupon = mockCoupon.issue(userId, currentTime);

            given(couponRepository.getCoupon(couponId)).willReturn(mockCoupon);
            given(issuedCouponRepository.findByCouponIdAndUserId(couponId, userId)).willReturn(null);
            given(issuedCouponRepository.save(any(IssuedCoupon.class))).willReturn(mockIssuedCoupon);

            // when
            IssuedCouponInfo.Coupon issuedCoupon = couponService.issue(couponId, userId, currentTime);

            // then
            assertThat(issuedCoupon).isNotNull();
            assertThat(issuedCoupon)
                    .extracting("name", "discountType", "discountAmt", "issuedAt", "validStartedAt", "validEndedAt", "usedAt")
                    .containsExactly(mockCoupon.getName(), mockCoupon.getDiscountType().name(), mockCoupon.getDiscountAmt(), currentTime, mockCoupon.getValidStartedAt(), mockCoupon.getValidEndedAt(), null);
        }
    }

    @Nested
    @DisplayName("쿠폰 조회 단위 테스트")
    class getCouponTest {

        @Test
        void 유저가_발급_받은_쿠폰_중_사용_가능한_쿠폰_목록을_조회한다() {
            // given
            Long userId = 1L;
            LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 1 ,0 ,0);
            Pageable pageable = PageRequest.of(0, 10);

            Coupon mockCoupon1 = Coupon.create("생일 쿠폰", DiscountType.FIXED_AMOUNT, new BigDecimal(3000), 3, 3,
                    LocalDateTime.of(2025, 1, 1, 0 ,0 ,0), LocalDateTime.of(2025, 1, 31, 23 ,59 ,59), CouponStatus.ACTIVE);
            Coupon mockCoupon2 = Coupon.create("첫 구매 감사 쿠폰", DiscountType.FIXED_AMOUNT, new BigDecimal(5000), 3, 3,
                    LocalDateTime.of(2025, 1, 5, 0 ,0 ,0), LocalDateTime.of(2025, 3, 31, 23 ,59 ,59), CouponStatus.ACTIVE);
            IssuedCoupon mockIssuedCoupon1 = mockCoupon1.issue(userId, currentTime);
            IssuedCoupon mockIssuedCoupon2 = mockCoupon2.issue(userId, currentTime);

            List<IssuedCoupon> issuedCoupons = List.of(mockIssuedCoupon1,mockIssuedCoupon2);

            Page<IssuedCoupon> mockUserCouponsPage = new PageImpl<>(issuedCoupons, pageable, 2);

            given(issuedCouponRepository.getPagedUserCoupons(userId, currentTime, pageable)).willReturn(mockUserCouponsPage);

            // when
            Page<IssuedCouponInfo.Coupon> userCouponsPage = couponService.getPagedUserCoupons(userId, currentTime, pageable);

            // then
            assertThat(userCouponsPage).isNotNull();
            assertThat(userCouponsPage.getTotalPages()).isEqualTo(1);
            assertThat(userCouponsPage.getTotalElements()).isEqualTo(2);
            assertThat(userCouponsPage.getContent().size()).isEqualTo(2);
            assertThat(userCouponsPage.getContent())
                    .extracting("name", "discountType", "discountAmt", "issuedAt", "validStartedAt", "validEndedAt", "usedAt")
                    .containsExactly(
                            tuple(mockCoupon1.getName(), mockCoupon1.getDiscountType().name(), mockCoupon1.getDiscountAmt(), currentTime, mockCoupon1.getValidStartedAt(), mockCoupon1.getValidEndedAt(), null),
                            tuple(mockCoupon2.getName(), mockCoupon2.getDiscountType().name(), mockCoupon2.getDiscountAmt(), currentTime, mockCoupon2.getValidStartedAt(), mockCoupon2.getValidEndedAt(), null)
                    );
        }

    }

    @Nested
    @DisplayName("쿠폰 사용 단위 테스트")
    class useCouponTest {
        @Test
        void 쿠폰_사용_시_쿠폰_번호_파라미터가_없다면_할인금액은_BigDecimal_ZERO를_반환한다() {
            // given
            Long isseudCouponId = null;
            BigDecimal totalOriginalAmt = new BigDecimal(3000);
            LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 1 ,0 ,0);

            // when
            BigDecimal discountAmt = couponService.useIssuedCoupon(isseudCouponId, totalOriginalAmt, currentTime);

            // then
            assertThat(discountAmt).isEqualTo(BigDecimal.ZERO);
        }

        @Test
        void 쿠폰_사용_시_발급_받은_쿠폰의_정보가_없다면_예외를_발생한다() {
            // given
            Long issuedCouponId = 1L;
            BigDecimal totalOriginalAmt = new BigDecimal(50000);
            LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 1 ,0 ,0);

            given(issuedCouponRepository.getIssuedCouponWithLock(issuedCouponId, currentTime)).willReturn(null);

            // when // then
            assertThatThrownBy(() -> couponService.useIssuedCoupon(issuedCouponId, totalOriginalAmt, currentTime))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.ISSUED_COUPON_NOT_FOUND.getMessage());
        }

        @Test
        void 쿠폰_사용_시_결제_금액보다_쿠폰_할인금액이_같거나_크면_예외를_발생한다() {
            // given
            Long userId = 1L;
            Long issuedCouponId = 1L;
            BigDecimal totalOriginalAmt = new BigDecimal(3000);
            LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 1 ,0 ,0);

            Coupon mockCoupon = Coupon.create("생일 쿠폰", DiscountType.FIXED_AMOUNT, new BigDecimal(3000), 3, 3,
                    LocalDateTime.of(2025, 1, 1, 0 ,0 ,0), LocalDateTime.of(2025, 1, 31, 23 ,59 ,59), CouponStatus.ACTIVE);
            IssuedCoupon mockIssuedCoupon = mockCoupon.issue(userId, currentTime);

            given(issuedCouponRepository.getIssuedCouponWithLock(issuedCouponId, currentTime)).willReturn(mockIssuedCoupon);

            // when
            assertThatThrownBy(() -> couponService.useIssuedCoupon(issuedCouponId, totalOriginalAmt, currentTime))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.COUPON_DISCOUNT_EXCEEDS_NET_AMOUNT.getMessage());
        }

        @Test
        void 정률_쿠폰_사용_시_쿠폰의_할인_금액을_계산해서_반환한다() {
            // given
            Long userId = 1L;
            Long issuedCouponId = 8L;
            BigDecimal totalOriginalAmt = new BigDecimal(50000L);
            LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 1 ,0 ,0);

            Coupon mockCoupon = Coupon.create("'블랙프라이데이 쿠폰'", DiscountType.PERCENTAGE, new BigDecimal(30), 3, 3,
                    LocalDateTime.of(2025, 1, 5, 0 ,0 ,0), LocalDateTime.of(2025, 1, 31, 23 ,59 ,59), CouponStatus.ACTIVE);
            IssuedCoupon mockIssuedCoupon = mockCoupon.issue(userId, currentTime);

            given(issuedCouponRepository.getIssuedCouponWithLock(issuedCouponId, currentTime)).willReturn(mockIssuedCoupon);

            // when
            BigDecimal discountAmt = couponService.useIssuedCoupon(issuedCouponId, totalOriginalAmt, currentTime);

            // then
            assertThat(discountAmt.compareTo((totalOriginalAmt.multiply(mockCoupon.getDiscountAmt())).divide(new BigDecimal(100)))).isEqualTo(0);

        }

        @Test
        void 정액_쿠폰_사용_시_쿠폰의_할인_금액을_계산해서_반환한다() {
            // given
            Long userId = 1L;
            Long issuedCouponId = 1L;
            BigDecimal totalOriginalAmt = new BigDecimal(50000L);
            LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 1 ,0 ,0);

            Coupon mockCoupon = Coupon.create("생일 쿠폰", DiscountType.FIXED_AMOUNT, new BigDecimal(3000), 3, 3,
                    LocalDateTime.of(2025, 1, 1, 0 ,0 ,0), LocalDateTime.of(2025, 1, 31, 23 ,59 ,59), CouponStatus.ACTIVE);
            IssuedCoupon mockIssuedCoupon = mockCoupon.issue(userId, currentTime);

            given(issuedCouponRepository.getIssuedCouponWithLock(issuedCouponId, currentTime)).willReturn(mockIssuedCoupon);

            // when
            BigDecimal discountAmt = couponService.useIssuedCoupon(issuedCouponId, totalOriginalAmt, currentTime);

            // then
            assertThat(discountAmt.compareTo(mockCoupon.getDiscountAmt())).isEqualTo(0);
        }
    }

}