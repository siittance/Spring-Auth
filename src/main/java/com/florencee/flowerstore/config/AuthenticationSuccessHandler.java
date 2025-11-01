package com.florencee.flowerstore.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Проверяем роль пользователя и перенаправляем на соответствующую страницу
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));
        boolean isManager = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("MANAGER"));
        boolean isCustomer = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("CUSTOMER"));

        if (isAdmin) {
            setDefaultTargetUrl("/admin/dashboard");
        } else if (isManager) {
            setDefaultTargetUrl("/manager/dashboard");
        } else if (isCustomer) {
            setDefaultTargetUrl("/customer/catalog");
        } else {
            setDefaultTargetUrl("/login");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}

