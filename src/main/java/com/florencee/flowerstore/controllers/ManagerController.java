package com.florencee.flowerstore.controllers;

import com.florencee.flowerstore.models.*;
import com.florencee.flowerstore.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/manager")
@PreAuthorize("hasAnyAuthority('MANAGER', 'ADMIN')")
public class ManagerController {

    @Autowired
    private FlowerRepository flowerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    // Панель менеджера
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Iterable<Order> allOrders = orderRepository.findAll();
        List<Order> pendingOrders = StreamSupport.stream(allOrders.spliterator(), false)
                .filter(o -> "PENDING".equals(o.getStatus()))
                .collect(Collectors.toList());
        model.addAttribute("pendingOrdersCount", pendingOrders.size());
        model.addAttribute("totalOrders", orderRepository.count());
        model.addAttribute("totalFlowers", flowerRepository.count());
        return "manager/dashboard";
    }

    // Управление цветами
    @GetMapping("/flowers")
    public String flowers(Model model) {
        model.addAttribute("flowers", flowerRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "manager/flowers";
    }

    @PostMapping("/flowers")
    public String createFlower(@RequestParam String name,
                              @RequestParam(required = false) String description,
                              @RequestParam BigDecimal price,
                              @RequestParam Integer stockQuantity,
                              @RequestParam(required = false) String color,
                              @RequestParam Long categoryId,
                              @RequestParam Long supplierId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Категория не найдена"));
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Поставщик не найден"));

        Flower flower = new Flower(name, description, price, stockQuantity, color, category, supplier);
        flowerRepository.save(flower);
        return "redirect:/manager/flowers";
    }

    @GetMapping("/flowers/{id}/update")
    public String updateFlowerView(@PathVariable Long id, Model model) {
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Цветок не найден"));
        model.addAttribute("flower", flower);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("suppliers", supplierRepository.findAll());
        return "manager/flower_update";
    }

    @PostMapping("/flowers/{id}/update")
    public String updateFlower(@PathVariable Long id,
                              @RequestParam String name,
                              @RequestParam(required = false) String description,
                              @RequestParam BigDecimal price,
                              @RequestParam Integer stockQuantity,
                              @RequestParam(required = false) String color,
                              @RequestParam Long categoryId,
                              @RequestParam Long supplierId) {
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Цветок не найден"));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Категория не найдена"));
        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Поставщик не найден"));

        flower.setName(name);
        flower.setDescription(description);
        flower.setPrice(price);
        flower.setStockQuantity(stockQuantity);
        flower.setColor(color);
        flower.setCategory(category);
        flower.setSupplier(supplier);
        flowerRepository.save(flower);
        return "redirect:/manager/flowers";
    }

    @GetMapping("/flowers/{id}/delete")
    public String deleteFlower(@PathVariable Long id) {
        flowerRepository.deleteById(id);
        return "redirect:/manager/flowers";
    }

    // Управление категориями
    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        return "manager/categories";
    }

    @PostMapping("/categories")
    public String createCategory(@RequestParam String name, @RequestParam(required = false) String description) {
        Category category = new Category(name, description);
        categoryRepository.save(category);
        return "redirect:/manager/categories";
    }

    // Управление заказами
    @GetMapping("/orders")
    public String orders(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "manager/orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));
        model.addAttribute("order", order);
        return "manager/order_detail";
    }

    @PostMapping("/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));
        order.setStatus(status);
        orderRepository.save(order);
        return "redirect:/manager/orders/" + id;
    }
}
