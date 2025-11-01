package com.florencee.flowerstore.controllers;

import com.florencee.flowerstore.models.Customer;
import com.florencee.flowerstore.models.RoleEnum;
import com.florencee.flowerstore.repos.CustomerRepository;
import com.florencee.flowerstore.util.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;

@Controller
public class RegistrationController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String registrationView(Model model) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(Customer customer, Model model) {
        if (customer.getUsername() == null || customer.getUsername().trim().isEmpty()) {
            model.addAttribute("error", "Имя пользователя обязательно для заполнения");
            return "registration";
        }

        if (customerRepository.existsByUsername(customer.getUsername())) {
            model.addAttribute("error", "Пользователь с таким логином уже существует");
            return "registration";
        }

        if (customer.getPassword() == null || customer.getPassword().trim().isEmpty()) {
            model.addAttribute("error", "Пароль обязателен для заполнения");
            return "registration";
        }

        // Валидация пароля
        PasswordValidator.ValidationResult validation = PasswordValidator.validate(customer.getPassword());
        if (!validation.isValid()) {
            model.addAttribute("error", validation.getMessage());
            return "registration";
        }

        customer.setActive(true);
        customer.setRoles(Collections.singleton(RoleEnum.CUSTOMER));
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customerRepository.save(customer);
        
        model.addAttribute("success", "Регистрация прошла успешно! Теперь вы можете войти.");
        return "redirect:/login?success";
    }
}
