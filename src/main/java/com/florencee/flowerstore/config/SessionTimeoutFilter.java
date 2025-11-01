package com.florencee.flowerstore.config;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Фильтр для отслеживания активности пользователя
 * Автоматически завершает сессию после 3 минут бездействия
 */
public class SessionTimeoutFilter implements Filter {

    // Таймаут бездействия: 3 минуты (180 секунд)
    private static final long INACTIVITY_TIMEOUT = 3 * 60 * 1000; // 3 минуты в миллисекундах

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Пропускаем публичные страницы и не выполняем проверку до того, как Spring Security обработает запрос
        String requestURI = httpRequest.getRequestURI();
        if (requestURI.startsWith("/login") || requestURI.startsWith("/registration") || 
            requestURI.startsWith("/css/") || requestURI.startsWith("/js/")) {
            chain.doFilter(request, response);
            return;
        }

        if (session != null && session.getAttribute("SPRING_SECURITY_CONTEXT") != null) {
            long lastActivityTime = session.getLastAccessedTime();
            long currentTime = System.currentTimeMillis();
            long inactiveTime = currentTime - lastActivityTime;

            // Если время бездействия превысило 3 минуты, инвалидируем сессию
            if (inactiveTime > INACTIVITY_TIMEOUT) {
                session.invalidate();
                httpResponse.sendRedirect("/login?expired");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
