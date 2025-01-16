package kr.hhplus.be.server.domain.point.service;

import kr.hhplus.be.server.domain.point.dto.info.PointInfo;
import kr.hhplus.be.server.domain.point.entity.Point;
import kr.hhplus.be.server.domain.point.entity.PointHistory;
import kr.hhplus.be.server.domain.point.repository.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.repository.PointRepository;
import kr.hhplus.be.server.domain.point.service.PointService;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.support.exception.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointServiceUnitTest {

    @InjectMocks
    private PointService pointService;

    @Mock
    private PointRepository pointRepository;

    @Mock
    private PointHistoryRepository pointHistoryRepository;

    Long userId = 1L;

    @Test
    void 유저_아이디로_포인트를_조회한다() {
        // given
        BigDecimal currentPoint = BigDecimal.valueOf(100);
        Point init = createPoint(currentPoint);

        given(pointRepository.findByUserIdOrThrow(userId)).willReturn(init);

        // when
        PointInfo.PointDto point = pointService.getPoint(userId);

        // then
        assertThat(point)
                .extracting("userId", "point")
                .containsExactly(init.getUserId(), init.getPoint());
    }

    @Test
    void 유저_아이디로_포인트_조회_시_포인트_정보가_없으면_CustomException_POINT_NOT_FOUND가_발생한다() {
        // given
        given(pointRepository.findByUserIdOrThrow(userId)).willThrow(new CustomException(ErrorCode.POINT_NOT_FOUND));

        // when // then
        assertThatThrownBy(() -> pointService.getPoint(userId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.POINT_NOT_FOUND.getMessage());
    }

    @Test
    void 포인트_충전_시_충전_요청_포인트가_0원_이하이면_CustomException_INVALID_CHARGE_POINT_AMOUNT가_발생한다() {
        // given
        BigDecimal currentPoint = BigDecimal.ZERO;
        BigDecimal chargePoint = BigDecimal.ZERO;

        Point point = createPoint(currentPoint);

        given(pointRepository.findByUserIdWithLock(userId)).willReturn(point);

        // when // then
        assertThatThrownBy(() -> pointService.charge(userId, chargePoint))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_CHARGE_POINT_AMOUNT.getMessage());
    }

    @Test
    void 포인트_충전_시_충전_요청_포인트가_1원_이상이면_정상적으로_합산된다() {
        // given
        BigDecimal currentPoint = BigDecimal.ZERO;
        BigDecimal chargePoint = BigDecimal.valueOf(100);

        Point point = createPoint(currentPoint);

        given(pointRepository.findByUserIdWithLock(userId)).willReturn(point);

        // when
        PointInfo.PointDto charge = pointService.charge(userId, chargePoint);

        // then
        assertThat(charge)
                .extracting("userId", "point")
                .containsExactly(userId, currentPoint.add(chargePoint));

        verify(pointRepository, times(1)).findByUserIdWithLock(userId);
    }

    @Test
    void 포인트_충전_시_포인트_충전_이력을_저장한다() {
        // given
        BigDecimal currentPoint = BigDecimal.ZERO;
        BigDecimal chargePoint = BigDecimal.valueOf(100);

        Point point = createPoint(currentPoint);

        given(pointRepository.findByUserIdWithLock(userId)).willReturn(point);
        given(pointHistoryRepository.save(any(PointHistory.class))).willReturn(null);

        // when
        pointService.charge(userId, chargePoint);

        verify(pointHistoryRepository, times(1)).save(any(PointHistory.class));
    }

    @Test
    void 포인트_사용_시_사용_요청_포인트가_0원_이하이면_CustomException_INVALID_USE_POINT_AMOUNT가_발생한다() {
        // given
        BigDecimal currentPoint = BigDecimal.ZERO;
        BigDecimal chargePoint = BigDecimal.ZERO;

        Point point = createPoint(currentPoint);

        given(pointRepository.findByUserIdWithLock(userId)).willReturn(point);

        // when // then
        assertThatThrownBy(() -> pointService.use(userId, chargePoint))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_USE_POINT_AMOUNT.getMessage());

        verify(pointRepository, times(1)).findByUserIdWithLock(userId);
    }

    @Test
    void 사용_요청_금액이_보유_잔액보다_크면_CustomException_INSUFFICIENT_POINT가_발생한다() {
        // given
        BigDecimal currentPoint = BigDecimal.ZERO;
        BigDecimal chargePoint = BigDecimal.ONE;

        Point point = createPoint(currentPoint);

        given(pointRepository.findByUserIdWithLock(userId)).willReturn(point);

        // when // then
        assertThatThrownBy(() -> pointService.use(userId, chargePoint))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INSUFFICIENT_POINT.getMessage());

        verify(pointRepository, times(1)).findByUserIdWithLock(userId);
    }

    @Test
    void 포인트_사용_시_포인트_사용_이력을_저장한다() {
        // given
        BigDecimal currentPoint = BigDecimal.valueOf(1000);
        BigDecimal userPoint = BigDecimal.valueOf(100);

        Point point = createPoint(currentPoint);

        given(pointRepository.findByUserIdWithLock(userId)).willReturn(point);
        given(pointHistoryRepository.save(any(PointHistory.class))).willReturn(null);

        // when
        pointService.use(userId, userPoint);

        // then
        verify(pointHistoryRepository, times(1)).save(any(PointHistory.class));
    }

    private static Point createPoint(BigDecimal point) {
        return Point.create(1L, point);
    }
}