package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.IntegrationTestSupport;
import kr.hhplus.be.server.domain.coupon.dto.info.IssuedCouponInfo;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.enums.CouponStatus;
import kr.hhplus.be.server.domain.coupon.enums.DiscountType;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.repository.IssuedCouponRepository;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class CouponServiceIntegrationTest extends IntegrationTestSupport {

    @Autowired
    CouponService couponService;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    IssuedCouponRepository issuedCouponRepository;

    @Nested
    @DisplayName("쿠폰 발급 통합 테스트")
    class IssueCouponTest {
        @Transactional
        @Test
        void 쿠폰을_발급한다() {
            // given
            LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 10, 0, 0);
            Long userId = 1L;
            Long couponId = 1L;

            Coupon coupon = couponRepository.findByIdWithLock(couponId);

            // when
            IssuedCouponInfo.Coupon issuedCoupon = couponService.issue(coupon.getId(), userId, currentTime);

            // then
            assertThat(issuedCoupon).isNotNull();
            assertThat(issuedCoupon)
                    .extracting("name", "discountType", "discountAmt", "issuedAt", "validStartedAt", "validEndedAt", "usedAt")
                    .containsExactly(coupon.getName(), coupon.getDiscountType().name(), coupon.getDiscountAmt(), currentTime, coupon.getValidStartedAt(), coupon.getValidEndedAt(), null);
        }

        @Transactional
        @Test
        void 쿠폰의_최대_발급_가능개수를_넘어가면_예외가_발생한다() {
            // given
            LocalDateTime currentTime = LocalDateTime.now();
            Long couponId = 1L;
            long userId = 1L;

            Coupon originCoupon = couponRepository.findByIdWithLock(couponId);

            couponService.issue(originCoupon.getId(), userId++, currentTime);
            couponService.issue(originCoupon.getId(), userId++, currentTime);
            couponService.issue(originCoupon.getId(), userId++, currentTime);

            long finalUserId = userId;

            // when // then
            assertThatThrownBy(() -> couponService.issue(originCoupon.getId(), finalUserId, currentTime))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.INSUFFICIENT_COUPON_QUANTITY.getMessage());
        }

        @Test
        void 동일한_유저가_쿠폰을_중복_발급하면_예외가_발생한다() {
            // given
            LocalDateTime currentTime = LocalDateTime.now();
            Long userId = 1L;
            Long couponId = 1L;

            couponService.issue(couponId, userId, currentTime);

            // when // then
            assertThatThrownBy(() -> couponService.issue(couponId, userId, currentTime))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.ALREADY_ISSUED_COUPON.getMessage());
        }
    }

    @Nested
    @DisplayName("쿠폰 조회 통합 테스트")
    class GetCouponTest {

        @Test
        void 유저가_발급_받은_쿠폰_중_사용_가능한_쿠폰_목록을_조회한다() {
            // given
            Long userId = 1L;
            LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 0 ,0 ,0);
            Pageable pageable = PageRequest.of(0, 10);

            Coupon coupon1 = Coupon.create("S 브랜드 할인 쿠폰", DiscountType.PERCENTAGE, new BigDecimal(10), 3, 3, LocalDateTime.of(2025,1,10,0,0,0)
            , LocalDateTime.of(2025,2,28,23,59,59), CouponStatus.ACTIVE);
            IssuedCoupon issuedCoupon1 = coupon1.issue(userId, currentTime);

            Coupon coupon2 = Coupon.create("블랙프라이데이 쿠폰", DiscountType.PERCENTAGE, new BigDecimal(30), 3, 3, LocalDateTime.of(2025,1,5,0,0,0)
                    , LocalDateTime.of(2025,1,10,00,00,01), CouponStatus.ACTIVE);
            IssuedCoupon issuedCoupon2 = coupon2.issue(userId, currentTime);

            // when
            Page<IssuedCouponInfo.Coupon> userCouponsPage = couponService.getPagedUserCoupons(userId, currentTime, pageable);

            // then
            assertThat(userCouponsPage).isNotNull();
            assertThat(userCouponsPage.getTotalPages()).isEqualTo(1);
            assertThat(userCouponsPage.getTotalElements()).isEqualTo(2);
            assertThat(userCouponsPage.getContent().size()).isEqualTo(2);
            assertThat(userCouponsPage)
                    .extracting("name", "discountType", "discountAmt", "issuedAt", "validStartedAt", "validEndedAt", "usedAt")
                    .containsExactly(
                            tuple(issuedCoupon1.getName(), issuedCoupon1.getDiscountType().name(), issuedCoupon1.getDiscountAmt().setScale(2, RoundingMode.HALF_UP), issuedCoupon1.getIssuedAt(), issuedCoupon1.getValidStartedAt(), issuedCoupon1.getValidEndedAt(), null),
                            tuple(issuedCoupon2.getName(), issuedCoupon2.getDiscountType().name(), issuedCoupon2.getDiscountAmt().setScale(2, RoundingMode.HALF_UP), issuedCoupon2.getIssuedAt(), issuedCoupon2.getValidStartedAt(), issuedCoupon2.getValidEndedAt(), null)
                    );
        }
    }

}
