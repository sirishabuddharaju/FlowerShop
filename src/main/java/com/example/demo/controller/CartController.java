package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.CartService;
import com.example.demo.service.OrderService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    // ========================= ADD TO CART ===============================
    @PostMapping("/cart/add")
    public String addToCart(
            @RequestParam Long flowerId,
            @RequestParam int quantity,
            HttpSession session
    ) {
        Long customerId = (Long) session.getAttribute("customerId");

        if (customerId == null) {
            return "redirect:/signin";
        }

        cartService.addToCart(customerId, flowerId, quantity);
        return "redirect:/cart";
    }


    // ========================= VIEW CART ===============================
    @GetMapping("/cart")
    public String showCart(HttpSession session, Model model) {

        Long customerId = (Long) session.getAttribute("customerId");

        if (customerId == null) {
            return "redirect:/signin";
        }

        model.addAttribute("cart", cartService.getCartItems(customerId));
        return "cart";
    }

    // ========================= PLACE ORDER ===============================
    @GetMapping("/cart/checkout")
    public String placeOrder(HttpSession session) {

        Long customerId = (Long) session.getAttribute("customerId");

        if (customerId == null) {
            return "redirect:/signin";
        }

        orderService.placeOrder(customerId);
        return "redirect:/orders";
    }

    // ========================= VIEW ORDERS ===============================
    @GetMapping("/orders")
    public String viewOrders(HttpSession session, Model model) {

        Long customerId = (Long) session.getAttribute("customerId");

        if (customerId == null) {
            return "redirect:/signin";
        }

        model.addAttribute("orders", orderService.getOrdersByCustomer(customerId));
        return "orders";
    }
}
