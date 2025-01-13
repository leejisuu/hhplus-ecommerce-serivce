package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.domain.user.dto.info.UserInfo;
import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import kr.hhplus.be.server.domain.user.service.UserService;
import kr.hhplus.be.server.interfaces.api.user.dto.UserPointChargeRequest;
import kr.hhplus.be.server.interfaces.api.user.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    void 포인트_충전_시_유저의_포인트가_정상적으로_합산된다() {
        // given
        Long userId = 1L;
        int chargeAmount = 1000;
        int point = 0;

        User user = createUser(point);

        given(userRepository.findByIdWithLock(userId))
                .willReturn(user);

        // when
        UserInfo userInfo = userService.addUserPoint(userId, chargeAmount);

        // then
        assertThat(userInfo)
                .extracting("id", "name", "point")
                .containsExactly(userId, user.getName(), point + chargeAmount);

        verify(userRepository, times(1)).findByIdWithLock(userId);
    }

    private static User createUser(int point) {
        return User.builder()
                .name("유저")
                .point(point)
                .build();
    }
}