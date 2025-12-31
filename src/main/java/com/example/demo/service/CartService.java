package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.CartItem;

public interface CartService {
    void addToCart(Long customerId, Long flowerId, int quantity);

    List<CartItem> getCartItems(Long customerId);

    void clearCart(Long customerId);
}
