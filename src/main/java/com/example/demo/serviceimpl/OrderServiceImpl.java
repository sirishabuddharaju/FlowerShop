package com.example.demo.serviceimpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.OrderTable;
import com.example.demo.entity.User;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderTableRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderTableRepository orderRepo;

    @Autowired
    private OrderItemRepository orderItemRepo;

    @Autowired
    private CartItemRepository cartRepo;

    @Autowired
    private UserRepository customerRepo;

    @Override
    @Transactional
    public OrderTable placeOrder(Long customerId) {

        // Fetch cart items for the customer
        List<CartItem> cartItems = cartRepo.findByCustomer_Id(customerId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty for customer id: " + customerId);
        }

        // Fetch Customer
        User customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Calculate Grand Total
        double grandTotal = cartItems.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();

        // Create Order
        OrderTable order = new OrderTable();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setGrandTotal(grandTotal);

        orderRepo.save(order);

        // Save Order Items
        for (CartItem ci : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setFlower(ci.getFlower());
            oi.setQuantity(ci.getQuantity());
            oi.setTotalPrice(ci.getTotalPrice());

            orderItemRepo.save(oi);
        }

        // Clear Cart AFTER placing order
        cartRepo.deleteByCustomer_Id(customerId);

        return order;
    }

    @Override
    public List<OrderTable> getOrdersByCustomer(Long customerId) {
        return orderRepo.findByCustomer_Id(customerId);
    }
}
