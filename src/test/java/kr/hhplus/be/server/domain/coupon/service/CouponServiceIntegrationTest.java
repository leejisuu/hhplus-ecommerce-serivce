package kr.hhplus.be.server.domain.coupon.service;

import kr.hhplus.be.server.domain.coupon.dto.CouponDto;
import kr.hhplus.be.server.domain.coupon.dto.command.CouponCommand;
import kr.hhplus.be.server.domain.coupon.dto.info.CouponInfo;
import kr.hhplus.be.server.domain.coupon.repository.CouponRepository;
import kr.hhplus.be.server.domain.coupon.repository.IssuedCouponRepository;
import kr.hhplus.be.server.support.IntegrationTestSupport;
import kr.hhplus.be.server.domain.coupon.dto.info.IssuedCouponInfo;
import kr.hhplus.be.server.domain.coupon.entity.Coupon;
import kr.hhplus.be.server.domain.coupon.entity.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.enums.CouponStatus;
import kr.hhplus.be.server.domain.coupon.enums.DiscountType;
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
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class CouponServiceIntegrationTest extends IntegrationTestSupport {

    @Autowired
    CouponService couponService;

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    IssuedCouponRepository issuedCouponRepository;

    @Nested
    @DisplayName("쿠폰 조회 통합 테스트")
    class AddCouponIssueRequestTest {
        @Test
        void 쿠폰_발급_요청_시_쿠폰_잔여_개수가_0_이하면_CustomException_INSUFFICIENT_COUPON_QUANTITY_예외를_발생한다() {
            // given
            long userId = 1L;
            long currentMillis = 1000;
            CouponCommand.Create createCommand = new CouponCommand.Create("웰컴 쿠폰", DiscountType.PERCENTAGE, BigDecimal.valueOf(15), 5, 0, LocalDateTime.of(2025, 2, 1, 0, 0, 0), LocalDateTime.of(2025, 2, 28, 23, 59, 59), CouponStatus.ACTIVE);
            CouponInfo.Create coupon = couponService.create(createCommand);

            CouponCommand.Issue issueCommand = new CouponCommand.Issue(coupon.id(), userId, currentMillis);

            // when // then
            assertThatThrownBy(() -> couponService.addCouponIssueRequest(issueCommand))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.INSUFFICIENT_COUPON_QUANTITY.getMessage());

        }

        @Test
        void 쿠폰_발급_요청_시_이미_발급받은_쿠폰이라면_CustomException_ALREADY_ISSUED_COUPON_예외를_발생한다() {
            // given
            long userId = 1L;
            long currentMillis = 1000;
            CouponCommand.Create createCommand = new CouponCommand.Create("웰컴 쿠폰", DiscountType.PERCENTAGE, BigDecimal.valueOf(15), 5, 1, LocalDateTime.of(2025, 2, 1, 0, 0, 0), LocalDateTime.of(2025, 2, 28, 23, 59, 59), CouponStatus.ACTIVE);
            CouponInfo.Create coupon = couponService.create(createCommand);

            CouponCommand.Issue issueCommand = new CouponCommand.Issue(coupon.id(), userId, currentMillis);

            couponService.addCouponIssueRequest(issueCommand);
            couponService.issue();

            // when // then
            assertThatThrownBy(() -> couponService.addCouponIssueRequest(issueCommand))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.INSUFFICIENT_COUPON_QUANTITY.getMessage());

        }

        @Test
        void 쿠폰_발급_요청_시_쿠폰_잔여_개수와_발급_요청을_Redis에_저장한다() {
            // given
            long userId = 1L;
            long currentMillis = 1000;
            int batchSize = 100;
            CouponCommand.Create createCommand = new CouponCommand.Create("웰컴 쿠폰", DiscountType.PERCENTAGE, BigDecimal.valueOf(15), 5, 5, LocalDateTime.of(2025, 2, 1, 0, 0, 0), LocalDateTime.of(2025, 2, 28, 23, 59, 59), CouponStatus.ACTIVE);
            CouponInfo.Create coupon = couponService.create(createCommand);

            CouponCommand.Issue issueCommand = new CouponCommand.Issue(coupon.id(), userId, currentMillis);

            // when
            couponService.addCouponIssueRequest(issueCommand);

            // then
            int remainCapacity = couponRepository.getRemainCapacityFromCache(coupon.id());
            List<CouponDto> issueRequest = couponRepository.getCouponIssueRequestsFromCache(batchSize);

            assertThat(remainCapacity).isEqualTo(coupon.remainCapacity()-1);
            assertThat(issueRequest).hasSize(1);
            assertThat(issueRequest.get(0))
                    .extracting("couponId", "userId", "currentMillis")
                    .containsExactly(coupon.id(), userId, currentMillis);
        }

        @Test
        void Redis에서_쿠폰_발급_요청을_가져와_쿠폰을_발급한다() {
            // given
            long userId = 1L;
            long currentMillis = 1000;
            CouponCommand.Create createCommand = new CouponCommand.Create("웰컴 쿠폰", DiscountType.PERCENTAGE, BigDecimal.valueOf(15), 5, 5, LocalDateTime.of(2025, 2, 1, 0, 0, 0), LocalDateTime.of(2025, 2, 28, 23, 59, 59), CouponStatus.ACTIVE);
            CouponInfo.Create info = couponService.create(createCommand);

            CouponCommand.Issue issueCommand = new CouponCommand.Issue(info.id(), userId, currentMillis);

            couponService.addCouponIssueRequest(issueCommand);

            // when
            couponService.issue();

            // then
            IssuedCoupon issuedCoupon = issuedCouponRepository.findByCouponIdAndUserId(info.id(), userId);
            Coupon coupon = couponRepository.getCoupon(info.id());

            assertThat(issuedCoupon).isNotNull();
            assertThat(coupon.getRemainCapacity()).isEqualTo(createCommand.remainCapacity()-1);
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
