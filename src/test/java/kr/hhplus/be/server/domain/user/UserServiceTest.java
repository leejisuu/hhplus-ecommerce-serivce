package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import kr.hhplus.be.server.interfaces.api.user.dto.UserPointChargeRequest;
import kr.hhplus.be.server.interfaces.api.user.dto.UserPointResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void 포인트_충전시_충전_포인트_합산_메서드를_실행한다() {
        // given
        Long userId = 1L;
        int chargeAmt = 1000;
        int point = 100;

        UserPointChargeRequest request = new UserPointChargeRequest(userId, chargeAmt);
        User user = new User("유저", point);

        given(userRepository.getUserWithLock(userId))
                .willReturn(user);

        // when
        UserPointResponse response = userService.chargeUserPoint(request);

        // then
        assertThat(response.point()).isEqualTo(point + chargeAmt);

        verify(userRepository, times(1)).getUserWithLock(userId);
    }
}