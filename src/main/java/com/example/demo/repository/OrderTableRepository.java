package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.OrderTable;


public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findByCustomer_Id(Long customerId);
}