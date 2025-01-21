package kr.hhplus.be.server.interfaces.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.hhplus.be.server.domain.support.exception.CustomException;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class UserValidationInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userIdStr = request.getHeader("X-USER-ID");
        if (userIdStr == null) {
            userIdStr = request.getParameter("userId");
        }

        if (userIdStr == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유저 정보가 없습니다.");
            return false;
        }

        Long userId;
        try {
            userId = Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "유효한 유저 ID가 아닙니다.");
            return false;
        }

        try {
            userRepository.findByUserIdOrThrow(userId);
        } catch (CustomException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "존재하지 않는 유저입니다.");
            return false;
        }

        return true;
    }


}
