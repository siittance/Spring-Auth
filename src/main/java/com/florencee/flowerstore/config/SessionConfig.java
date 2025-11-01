package com.florencee.flowerstore.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Configuration
public class SessionConfig implements HttpSessionListener {

    // Максимальное время жизни сессии: 15 минут (900 секунд)
    private static final int MAX_SESSION_INTERVAL = 15 * 60;

    @Bean
    public ServletListenerRegistrationBean<HttpSessionListener> sessionListener() {
        return new ServletListenerRegistrationBean<>(this);
    }

    @Bean
    public FilterRegistrationBean<SessionTimeoutFilter> sessionTimeoutFilter() {
        FilterRegistrationBean<SessionTimeoutFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new SessionTimeoutFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(100); // Устанавливаем более низкий приоритет, чтобы Security Filter обрабатывал запросы первым
        return registrationBean;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // Устанавливаем максимальное время жизни сессии: 15 минут
        se.getSession().setMaxInactiveInterval(MAX_SESSION_INTERVAL);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // Логирование уничтожения сессии (опционально)
    }
}
