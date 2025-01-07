package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.support.exception.CustomException;
import kr.hhplus.be.server.support.exception.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class UserTest {

    @Test
    void 충전_요청_금액이_0원_이하면_CustomError가_발생한다() {
        // given
        User user = createUser(0);

        // when // then
        assertThatThrownBy(() -> user.addPoint(0))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_POINT_AMOUNT.getMessage());
    }

    @Test
    void 충전_요청_금액이_1원_이상이면_포인트를_합산한다() {
        // given
        User user = createUser(0);

        // when
        user.addPoint(1);

        // then
        assertThat(user.getPoint()).isEqualTo(1);
    }

    @Test
    void 사용_요청_금액이_보유_잔액보다_크면_CustomError가_발생한다() {
        // given
        User user = createUser(1000);

        // when // then
        assertThatThrownBy(() -> user.deductPoint(1001))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INSUFFICIENT_POINT.getMessage());
    }

    @Test
    void 사용_요청_금액이_보유_잔액_이하면_포인트가_차감된다() {
        // given
        User user = createUser(1000);

        // when
        user.deductPoint(1000);

        // then
        assertThat(user.getPoint()).isEqualTo(0);
    }

    private static User createUser(int point) {
        return User.builder()
                .name("유저")
                .point(point)
                .build();
    }
}