package com.florencee.flowerstore.controllers;

import com.florencee.flowerstore.models.Category;
import com.florencee.flowerstore.models.Customer;
import com.florencee.flowerstore.models.Flower;
import com.florencee.flowerstore.models.Supplier;
import com.florencee.flowerstore.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class AdminController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FlowerRepository flowerRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    // Административная панель
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("customersCount", customerRepository.count());
        model.addAttribute("categoriesCount", categoryRepository.count());
        model.addAttribute("flowersCount", flowerRepository.count());
        model.addAttribute("suppliersCount", supplierRepository.count());
        return "admin/dashboard";
    }

    // Управление пользователями
    @GetMapping("/customers")
    public String customers(Model model) {
        model.addAttribute("customers", customerRepository.findAll());
        return "admin/customers";
    }

    @GetMapping("/customers/{id}")
    public String customerDetail(@PathVariable Long id, Model model) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Покупатель не найден: " + id));
        model.addAttribute("customer", customer);
        return "admin/customer_detail";
    }

    // Управление категориями
    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/categories";
    }

    @PostMapping("/categories")
    public String createCategory(@RequestParam String name, @RequestParam(required = false) String description) {
        Category category = new Category(name, description);
        categoryRepository.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/categories/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/admin/categories";
    }

    // Управление поставщиками
    @GetMapping("/suppliers")
    public String suppliers(Model model) {
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "admin/suppliers";
    }

    @PostMapping("/suppliers")
    public String createSupplier(@RequestParam String name,
                                @RequestParam(required = false) String contactPerson,
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) String phone,
                                @RequestParam(required = false) String address) {
        Supplier supplier = new Supplier(name, contactPerson, email, phone, address);
        supplierRepository.save(supplier);
        return "redirect:/admin/suppliers";
    }

    @GetMapping("/suppliers/{id}/delete")
    public String deleteSupplier(@PathVariable Long id) {
        supplierRepository.deleteById(id);
        return "redirect:/admin/suppliers";
    }
}
