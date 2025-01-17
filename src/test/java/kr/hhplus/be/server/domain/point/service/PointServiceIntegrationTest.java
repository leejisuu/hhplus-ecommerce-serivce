package kr.hhplus.be.server.domain.point.service;

import kr.hhplus.be.server.DatabaseCleanup;
import kr.hhplus.be.server.IntegrationTestSupport;
import kr.hhplus.be.server.domain.point.dto.info.PointInfo;
import kr.hhplus.be.server.domain.point.entity.Point;
import kr.hhplus.be.server.domain.point.service.PointService;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import kr.hhplus.be.server.infrastructure.point.PointJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PointServiceIntegrationTest extends IntegrationTestSupport {

    @Autowired
    PointService pointService;

    @Autowired
    PointJpaRepository pointJpaRepository;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    void databaseCleanup() {
        databaseCleanup.execute();
    }

    @Test
    void 유저_아이디로_포인트를_조회한다() {
        // given
        Long userId = 1L;
        BigDecimal currentPoint = BigDecimal.valueOf(100);
        Point init = Point.create(userId, currentPoint);
        pointJpaRepository.save(init);

        // when
        PointInfo.PointDto point = pointService.getPoint(userId);

        // then
        assertThat(point.userId()).isEqualTo(userId);
        assertThat(currentPoint.compareTo(point.point())).isEqualTo(0);
    }

    @Test
    void 유저_아이디로_포인트_조회_시_포인트_정보가_없으면_예외를_발생한다() {
        // given
        Long userId = 1L;

        // when // then
        assertThatThrownBy(() -> pointService.getPoint(userId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.POINT_NOT_FOUND.getMessage());
    }

    @Test
    void 포인트를_충전_요청_시_요청_포인트가_0원_이하이면_예외를_발생한다() {
        // given
        Long userId = 1L;
        BigDecimal currentPoint = BigDecimal.ZERO;
        BigDecimal chargePoint = BigDecimal.ZERO;

        Point init = Point.create(userId, currentPoint);
        pointJpaRepository.save(init);

        // when // then
        assertThatThrownBy(() -> pointService.charge(userId, chargePoint))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_CHARGE_POINT_AMOUNT.getMessage());
    }

    @Test
    void 포인트를_충전_요청_시_보유_포인트에서_충전_요청_포인트를_합산하여_저장한다() {
        // given
        Long userId = 1L;
        BigDecimal currentPoint = BigDecimal.ZERO;
        BigDecimal chargePoint = BigDecimal.valueOf(1000);

        Point init = Point.create(userId, currentPoint);
        pointJpaRepository.save(init);

        // when
        PointInfo.PointDto savedPoint = pointService.charge(userId, chargePoint);

        // then
        Assertions.assertThat(savedPoint.userId()).isEqualTo(userId);
        Assertions.assertThat(savedPoint.point().compareTo(currentPoint.add(chargePoint))).isEqualTo(0);
    }

    @Test
    void 포인트를_사용_요청_시_요청_포인트가_0원_이하이면_예외를_발생한다() {
        // given
        Long userId = 1L;
        BigDecimal currentPoint = BigDecimal.ZERO;
        BigDecimal usePoint = BigDecimal.ZERO;

        Point init = Point.create(userId, currentPoint);
        pointJpaRepository.save(init);

        // when // then
        assertThatThrownBy(() -> pointService.use(userId, usePoint))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_USE_POINT_AMOUNT.getMessage());
    }

    @Test
    void 사용_요청_금액이_보유_잔액보다_크면_CustomException_INSUFFICIENT_POINT가_발생한다() {
        // given
        Long userId = 1L;
        BigDecimal currentPoint = BigDecimal.valueOf(100);
        BigDecimal usePoint = BigDecimal.valueOf(101);

        Point init = Point.create(userId, currentPoint);
        pointJpaRepository.save(init);

        // when // then
        assertThatThrownBy(() -> pointService.use(userId, usePoint))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INSUFFICIENT_POINT.getMessage());
    }

    @Test
    void 포인트를_사용_요청_시_보유_포인트에서_사용_요청_포인트를_차감하여_저장한다() {
        // given
        Long userId = 1L;
        BigDecimal currentPoint = BigDecimal.ZERO;
        BigDecimal chargePoint = BigDecimal.valueOf(1000);

        Point init = Point.create(userId, currentPoint);
        pointJpaRepository.save(init);

        // when
        PointInfo.PointDto savedPoint = pointService.charge(userId, chargePoint);

        // then
        Assertions.assertThat(savedPoint.userId()).isEqualTo(userId);
        Assertions.assertThat(savedPoint.point().compareTo(currentPoint.add(chargePoint))).isEqualTo(0);
    }

}
