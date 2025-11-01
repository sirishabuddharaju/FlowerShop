package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Flower;

public interface FlowerService {

	
    // Get all flowers
    List<Flower> findAll();

    // Get flower by ID
    Optional<Flower> findById(Long id);

    // Save a new flower
    Flower saveFlower(Flower flower);

    // Update an existing flower
    Flower updateFlower(Long id, Flower flower);

    // Delete flower by ID
    void deleteById(Long id);

    // Search flowers by name
    List<Flower> searchByName(String q);
}
