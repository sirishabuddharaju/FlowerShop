package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.Admin;
import com.example.demo.service.AdminService;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    // Show login page
    @GetMapping("/adminsignin")
    public String showSignInForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "adminsignin";  // corresponds to adminsignin.html
    }

    // Handle login form
    @PostMapping("/adminsignin")
    public String loginAdmin(@ModelAttribute Admin admin, Model model) {
        return adminService.loginAdmin(admin.getEmail(), admin.getPassword())
                .map(a -> "redirect:/admin/home") // success → redirect to admin home
                .orElseGet(() -> {
                    model.addAttribute("error", "Invalid credentials! Try again.");
                    return "adminsignin"; // stay on same page with error
                });
    }

    // Admin home after successful login
    @GetMapping("/adminhome")
    public String adminHome() {
        return "adminHome"; // admin dashboard page
    }
}
