package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Flower;

public interface FlowerRepository extends JpaRepository<Flower, Long> {
    List<Flower> findByNameContainingIgnoreCase(String name);
}
