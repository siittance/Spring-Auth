package com.florencee.flowerstore.config;

import com.florencee.flowerstore.models.Customer;
import com.florencee.flowerstore.models.RoleEnum;
import com.florencee.flowerstore.repos.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Создаем администратора, если его еще нет
        if (!customerRepository.existsByUsername("admin")) {
            Customer admin = new Customer();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("Qwerty123!"));
            admin.setFullName("Администратор системы");
            admin.setEmail("admin@example.com");
            admin.setActive(true);
            Set<RoleEnum> adminRoles = new HashSet<>();
            adminRoles.add(RoleEnum.ADMIN);
            admin.setRoles(adminRoles);
            customerRepository.save(admin);
            System.out.println("==========================================");
            System.out.println("Администратор создан успешно!");
            System.out.println("Логин: admin");
            System.out.println("Пароль: Qwerty123!");
            System.out.println("Email: admin@example.com");
            System.out.println("==========================================");
        }

        // Создаем тестового менеджера, если его еще нет
        if (!customerRepository.existsByUsername("manager")) {
            Customer manager = new Customer();
            manager.setUsername("manager");
            manager.setPassword(passwordEncoder.encode("Manager123!"));
            manager.setFullName("Менеджер магазина");
            manager.setEmail("manager@florencee.ru");
            manager.setActive(true);
            Set<RoleEnum> managerRoles = new HashSet<>();
            managerRoles.add(RoleEnum.MANAGER);
            manager.setRoles(managerRoles);
            customerRepository.save(manager);
            System.out.println("Менеджер создан: manager / Manager123!");
        }

        // Создаем тестового покупателя, если его еще нет
        if (!customerRepository.existsByUsername("customer")) {
            Customer customer = new Customer();
            customer.setUsername("customer");
            customer.setPassword(passwordEncoder.encode("Customer123!"));
            customer.setFullName("Тестовый покупатель");
            customer.setEmail("customer@florencee.ru");
            customer.setActive(true);
            Set<RoleEnum> customerRoles = new HashSet<>();
            customerRoles.add(RoleEnum.CUSTOMER);
            customer.setRoles(customerRoles);
            customerRepository.save(customer);
            System.out.println("Покупатель создан: customer / Customer123!");
        }
    }
}

