package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.OrderTable;

public interface OrderService {
    OrderTable placeOrder(Long customerId);

    List<OrderTable> getOrdersByCustomer(Long customerId);
}
