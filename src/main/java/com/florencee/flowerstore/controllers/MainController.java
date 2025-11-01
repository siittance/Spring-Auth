package com.florencee.flowerstore.controllers;

import com.florencee.flowerstore.models.Customer;
import com.florencee.flowerstore.repos.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            // Получаем роли пользователя
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ADMIN"));
            boolean isManager = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("MANAGER"));
            boolean isCustomer = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("CUSTOMER"));
            
            // Определяем роль и перенаправляем на соответствующую страницу
            if (isAdmin) {
                return "redirect:/admin/dashboard";
            } else if (isManager) {
                return "redirect:/manager/dashboard";
            } else if (isCustomer) {
                return "redirect:/customer/catalog";
            }
        }
        return "redirect:/login";
    }
}
