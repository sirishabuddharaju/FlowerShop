package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // 🌸 Landing page
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 👩‍💼 Admin Page
    @GetMapping("/admin")
    public String adminPage() {
        return "adminpage";
    }

    // 👩‍🦰 Customer Page
    @GetMapping("/customer")
    public String customerPage() {
        return "customer";
    }

    // 🌷 Register Form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // 🌷 Handle Registration
    @PostMapping("/register")
    public String registerUser(
            @Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model model) {

        // Validation errors
        if (result.hasErrors()) {
            return "register";
        }

        // Password mismatch
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("passwordError", "Passwords do not match!");
            return "register";
        }

        // Duplicate email check
        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("emailError", "Email already registered. Please use another email.");
            return "register";
        }

        // Save new user
        userService.registerUser(user);
        model.addAttribute("successMessage", "Registration successful! Please sign in below.");
        model.addAttribute("user", new User()); // reset form
        return "register";
    }

    // 🌸 Show Sign-in form
    @GetMapping("/signin")
    public String showSignInForm(Model model) {
        model.addAttribute("user", new User());
        return "signin";
    }

    // 🌸 Handle Sign-in
    @PostMapping("/signin")
    public String loginUser(@ModelAttribute("user") User user, Model model) {
        Optional<User> existingUser = userService.findByEmail(user.getEmail());

        // 1️⃣ Email not found
        if (existingUser.isEmpty()) {
            model.addAttribute("loginError", "❌ User not registered with this email.");
            return "signin";
        }

        // 2️⃣ Password mismatch
        User dbUser = existingUser.get();
        if (!dbUser.getPassword().equals(user.getPassword())) {
            model.addAttribute("loginError", "⚠️ Incorrect password. Please try again.");
            return "signin";
        }

        // 3️⃣ Successful login
        return "redirect:/home";
    }

 // 🌼 Forgot Password Page
    @GetMapping("/forgot-password")
    public String showForgotPasswordPage(Model model) {
        model.addAttribute("user", new User());
        return "forgotpassword";
    }

    @PostMapping("/forgot-password")
    public String resetPassword(@ModelAttribute("user") User user, Model model) {
        Optional<User> existingUser = userService.findByEmail(user.getEmail());

        if (existingUser.isEmpty()) {
            model.addAttribute("emailError", "Email not registered!");
            return "forgotpassword";
        }

        userService.updateUserDetails(user.getEmail(), user);
        model.addAttribute("successMessage", "Password updated successfully! Please sign in.");
        return "redirect:/signin";
    }

    // 🌺 Home page
    @GetMapping("/home")
    public String homePage() {
        return "home";
    }
}
