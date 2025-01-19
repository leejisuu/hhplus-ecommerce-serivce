package kr.hhplus.be.server.domain.point.service;

import kr.hhplus.be.server.IntegrationTestSupport;
import kr.hhplus.be.server.domain.point.dto.info.PointInfo;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PointServiceIntegrationTest extends IntegrationTestSupport {

    @Autowired
    PointService pointService;

    @Nested
    @DisplayName("포인트 조회 통합 테스트")
    class GetPointTest {
        @Test
        void 유저_아이디로_포인트를_조회한다() {
            // given
            Long userId = 1L;
            BigDecimal point = BigDecimal.valueOf(2500);

            // when
            PointInfo.PointDto currentPoint = pointService.getPoint(userId);

            // then
            assertThat(currentPoint.point().compareTo(point)).isEqualTo(0);
        }

        @Test
        void 유저_아이디로_포인트_조회_시_포인트_정보가_없으면_예외를_발생한다() {
            // given
            Long userId = 6L;

            // when // then
            assertThatThrownBy(() -> pointService.getPoint(userId))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.POINT_NOT_FOUND.getMessage());
        }
    }

    @Nested
    @DisplayName("포인트 충전 통합 테스트")
    class ChargePointTest {
        @Test
        void 포인트를_충전_요청_시_요청_포인트가_0원_이하이면_예외를_발생한다() {
            // given
            Long userId = 1L;
            BigDecimal amount = BigDecimal.ZERO;

            // when // then
            assertThatThrownBy(() -> pointService.charge(userId, amount))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.INVALID_CHARGE_POINT_AMOUNT.getMessage());
        }

        @Test
        void 포인트를_충전_요청_시_보유_포인트에서_충전_요청_포인트를_합산하여_저장한다() {
            // given
            Long userId = 1L;
            BigDecimal amount = BigDecimal.valueOf(1000);
            PointInfo.PointDto currentPoint = pointService.getPoint(userId);

            // when
            PointInfo.PointDto chargedPoint = pointService.charge(userId, amount);

            // then
            assertThat(chargedPoint.userId()).isEqualTo(userId);
            assertThat(chargedPoint.point().compareTo(currentPoint.point().add(amount))).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("포인트 사용 통합 테스트")
    class UsePointTest {
        @Test
        void 포인트를_사용_요청_시_요청_포인트가_0원_이하이면_예외를_발생한다() {
            // given
            Long userId = 1L;
            BigDecimal amount = BigDecimal.ZERO;

            // when // then
            assertThatThrownBy(() -> pointService.use(userId, amount))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.INVALID_USE_POINT_AMOUNT.getMessage());
        }

        @Test
        void 사용_요청_금액이_보유_잔액보다_크면_CustomException_INSUFFICIENT_POINT가_발생한다() {
            // given
            Long userId = 1L;

            PointInfo.PointDto currentPoint = pointService.getPoint(userId);
            BigDecimal amount = currentPoint.point().add(BigDecimal.ONE);

            // when // then
            assertThatThrownBy(() -> pointService.use(userId, amount))
                    .isInstanceOf(CustomException.class)
                    .hasMessage(ErrorCode.INSUFFICIENT_POINT.getMessage());
        }

        @Test
        void 포인트를_사용_요청_시_보유_포인트에서_사용_요청_포인트를_차감하여_저장한다() {
            // given
            Long userId = 1L;
            BigDecimal amount = BigDecimal.valueOf(1000);

            PointInfo.PointDto currentPoint = pointService.getPoint(userId);

            // when
            PointInfo.PointDto savedPoint = pointService.use(userId, amount);

            // then
            Assertions.assertThat(savedPoint.point().compareTo(currentPoint.point().subtract(amount))).isEqualTo(0);
        }
    }
}
