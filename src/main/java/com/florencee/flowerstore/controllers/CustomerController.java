package com.florencee.flowerstore.controllers;

import com.florencee.flowerstore.models.*;
import com.florencee.flowerstore.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/customer")
@PreAuthorize("hasAnyAuthority('CUSTOMER', 'MANAGER', 'ADMIN')")
public class CustomerController {

    @Autowired
    private FlowerRepository flowerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    // Каталог цветов
    @GetMapping("/catalog")
    public String catalog(@RequestParam(required = false) Long categoryId, Model model) {
        List<Flower> flowers;
        if (categoryId != null) {
            flowers = flowerRepository.findByCategoryId(categoryId);
        } else {
            flowers = (List<Flower>) flowerRepository.findAll();
        }
        model.addAttribute("flowers", flowers);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("selectedCategoryId", categoryId);
        return "customer/catalog";
    }

    @GetMapping("/catalog/{id}")
    public String flowerDetail(@PathVariable Long id, Model model) {
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Цветок не найден"));
        model.addAttribute("flower", flower);
        return "customer/flower_detail";
    }

    // Создание заказа
    @PostMapping("/catalog/{id}/order")
    public String createOrder(@PathVariable Long id,
                             @RequestParam Integer quantity,
                             @RequestParam(required = false) String deliveryAddress,
                             @RequestParam(required = false) String notes,
                             Authentication authentication) {
        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Цветок не найден"));

        if (flower.getStockQuantity() < quantity) {
            return "redirect:/customer/catalog/" + id + "?error=Недостаточно товара на складе";
        }

        Customer customer = customerRepository.findByUsername(authentication.getName());
        String address = deliveryAddress != null && !deliveryAddress.isEmpty() 
                ? deliveryAddress 
                : customer.getAddress();

        Order order = new Order();
        order.setCustomer(customer);
        order.setDeliveryAddress(address);
        order.setNotes(notes);
        order.setStatus("PENDING");
        order = orderRepository.save(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setFlower(flower);
        orderItem.setQuantity(quantity);
        orderItem.setUnitPrice(flower.getPrice());
        orderItem = orderItemRepository.save(orderItem);

        // Обновляем количество на складе
        flower.setStockQuantity(flower.getStockQuantity() - quantity);
        flowerRepository.save(flower);

        // Рассчитываем общую сумму заказа
        BigDecimal totalAmount = orderItem.getSubtotal();
        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        return "redirect:/customer/orders";
    }

    // Мои заказы
    @GetMapping("/orders")
    public String myOrders(Authentication authentication, Model model) {
        Customer customer = customerRepository.findByUsername(authentication.getName());
        List<Order> orders = orderRepository.findByCustomerId(customer.getId());
        model.addAttribute("orders", orders);
        return "customer/orders";
    }

    @GetMapping("/orders/{id}")
    public String orderDetail(@PathVariable Long id, Authentication authentication, Model model) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Заказ не найден"));
        Customer customer = customerRepository.findByUsername(authentication.getName());

        // Проверяем, что заказ принадлежит текущему покупателю
        if (!order.getCustomer().getId().equals(customer.getId())) {
            return "redirect:/customer/orders?error=Доступ запрещен";
        }

        model.addAttribute("order", order);
        return "customer/order_detail";
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        Customer customer = customerRepository.findByUsername(authentication.getName());
        model.addAttribute("customer", customer);
        return "customer/profile";
    }
}
