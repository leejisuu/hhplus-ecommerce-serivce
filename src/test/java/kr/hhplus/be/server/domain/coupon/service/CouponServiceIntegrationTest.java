package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.support.IntegrationTestSupport;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class CouponServiceIntegrationTest extends IntegrationTestSupport {

    @Autowired
    CouponService couponService;

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
                            tuple(issuedCoupon1.getName(), issuedCoupon1.getDiscountType().name(), setScaleFromBigDecimal(issuedCoupon1.getDiscountAmt()), issuedCoupon1.getIssuedAt(), issuedCoupon1.getValidStartedAt(), issuedCoupon1.getValidEndedAt(), null),
                            tuple(issuedCoupon2.getName(), issuedCoupon2.getDiscountType().name(), setScaleFromBigDecimal(issuedCoupon2.getDiscountAmt()), issuedCoupon2.getIssuedAt(), issuedCoupon2.getValidStartedAt(), issuedCoupon2.getValidEndedAt(), null)
                    );
        }
    }

    @Nested
    @DisplayName("쿠폰 사용 테스트")
    class UseCouponTest {

        @Test
        void 쿠폰_사용_시_쿠폰_번호_정보가_없다면_할인금액_BigDecimal_ZERO을_반환한다() {
            // given
            Long issuedCouponId = null;
            BigDecimal totalOriginalAmt = new BigDecimal(3000);
            LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 1, 0, 0);

            // when
            BigDecimal discountAmt = couponService.useIssuedCoupon(issuedCouponId, totalOriginalAmt, currentTime);

            // then
            assertThat(discountAmt).isEqualTo(BigDecimal.ZERO);
        }

        @Test
        void 발급받은_쿠폰_정보가_없다면_CustomException_ISSUED_COUPON_NOT_FOUND_예외를_발생한다() {
            // given
            Long issuedCouponId = 6L;
            BigDecimal totalOriginalAmt = new BigDecimal(3000);
            LocalDateTime currentTime = LocalDateTime.of(2025, 1, 10, 1, 0, 0);

            // when // then
            assertThatThrownBy(() -> couponService.useIssuedCoupon(issuedCouponId, totalOriginalAmt, currentTime))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.ISSUED_COUPON_NOT_FOUND.getMessage());
        }
    }

    private static BigDecimal setScaleFromBigDecimal(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
