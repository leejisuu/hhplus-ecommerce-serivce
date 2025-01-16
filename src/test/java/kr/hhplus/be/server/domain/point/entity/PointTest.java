package kr.hhplus.be.server.domain.point.entity;

import kr.hhplus.be.server.domain.point.entity.Point;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class PointTest {

    @Test
    void 충전_요청_금액이_0원_이하면_CustomException_INVALID_CHARGE_POINT_AMOUNT가_발생한다() {
        // given
        BigDecimal currentPoint = BigDecimal.ZERO;
        BigDecimal chargePoint = BigDecimal.ZERO;
        Point point = createPoint(currentPoint);

        // when // then
        assertThatThrownBy(() -> point.addPoint(chargePoint))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_CHARGE_POINT_AMOUNT.getMessage());
    }

    @Test
    void 충전_요청_금액이_1원_이상이면_포인트를_합산한다() {
        // given
        BigDecimal currentPoint = BigDecimal.ZERO;
        BigDecimal chargePoint = BigDecimal.ONE;
        Point point = createPoint(currentPoint);

        // when
        point.addPoint(chargePoint);

        // then
        assertThat(point.getPoint()).isEqualTo(currentPoint.add(chargePoint));
    }

    @Test
    void 사용_요청_금액이_0원_이하면_CustomException_INVALID_USE_POINT_AMOUNT가_발생한다() {
        // given
        BigDecimal currentPoint = BigDecimal.valueOf(1000);
        BigDecimal userPoint = BigDecimal.ZERO;
        Point point = createPoint(currentPoint);

        // when // then
        assertThatThrownBy(() -> point.deductPoint(userPoint))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_USE_POINT_AMOUNT.getMessage());
    }

    @Test
    void 사용_요청_금액이_보유_잔액보다_크면_CustomException_INSUFFICIENT_POINT가_발생한다() {
        // given
        BigDecimal currentPoint = BigDecimal.valueOf(1000);
        BigDecimal userPoint = BigDecimal.valueOf(1001);
        Point point = createPoint(currentPoint);

        // when // then
        assertThatThrownBy(() -> point.deductPoint(userPoint))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INSUFFICIENT_POINT.getMessage());
    }

    @Test
    void 사용_요청_금액이_보유_잔액_이하면_포인트가_차감된다() {
        // given
        BigDecimal currentPoint = BigDecimal.valueOf(1000);
        BigDecimal userPoint = BigDecimal.valueOf(1000);
        Point point = createPoint(currentPoint);

        // when
        point.deductPoint(userPoint);

        // then
        assertThat(point.getPoint()).isEqualTo(BigDecimal.ZERO);
    }

    private static Point createPoint(BigDecimal amount) {
        return Point.builder()
                .userId(1L)
                .point(amount)
                .build();
    }
}