package com.example.demo.service;

import java.util.Optional;
import com.example.demo.entity.Admin;

public interface AdminService {
    Optional<Admin> loginAdmin(String email, String password);
}
