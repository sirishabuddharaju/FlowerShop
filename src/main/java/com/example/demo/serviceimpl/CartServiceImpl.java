package com.example.demo.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.CartItem;
import com.example.demo.entity.Flower;
import com.example.demo.entity.User;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.FlowerRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CartService;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private UserRepository customerRepo;

    @Autowired
    private FlowerRepository flowerRepo;

    @Autowired
    private CartItemRepository cartRepo;

    @Override
    public void addToCart(Long customerId, Long flowerId, int quantity) {

        User customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer Not Found"));

        Flower flower = flowerRepo.findById(flowerId)
                .orElseThrow(() -> new RuntimeException("Flower Not Found"));

        double total = flower.getPrice() * quantity;

        CartItem item = new CartItem();
        item.setCustomer(customer);
        item.setFlower(flower);
        item.setQuantity(quantity);
        item.setTotalPrice(total);

        cartRepo.save(item);
    }

    @Override
    public List<CartItem> getCartItems(Long customerId) {
        return cartRepo.findByCustomer_Id(customerId);
    }

    @Override
    public void clearCart(Long customerId) {
        cartRepo.deleteByCustomer_Id(customerId);
    }
}

