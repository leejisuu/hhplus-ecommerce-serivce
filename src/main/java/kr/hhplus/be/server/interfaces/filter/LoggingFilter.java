package kr.hhplus.be.server.interfaces.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String method = httpRequest.getMethod();
        String path = httpRequest.getRequestURI();

        // 데이터 변경이 일어나는 요청만 로깅
        if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "DELETE".equalsIgnoreCase(method)) {
            log.info("[요청] {} {}", method, path);
        }

        // 요청 처리 진행
        chain.doFilter(request, response);

        int status = httpResponse.getStatus();

        // 실패한 요청은(4xx, 5xx) 응답도 로깅
        if (status >= 400) {
            log.warn("[응답] {} {} -> 상태: {}", method, path, status);
        }

    }
}
