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

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // index page
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // Admin Page
    @GetMapping("/admin")
    public String adminPage() {
        return "adminpage";
    }

    // Customer Page
    @GetMapping("/customer")
    public String customerPage() {
        return "customer";
    }

    //  Register Form
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    //  Handle Registration
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

    //  Show Sign-in form
    @GetMapping("/signin")
    public String showSignInForm(Model model) {
        model.addAttribute("user", new User());
        return "signin";
    }

    //  Handle Sign-in
    @PostMapping("/signin")
    public String loginUser(@ModelAttribute("user") User user, Model model, HttpSession session) {

        Optional<User> existingUser = userService.findByEmail(user.getEmail());

        if (existingUser.isEmpty()) {
            model.addAttribute("loginError", " User not registered with this email.");
            return "signin";
        }

        User dbUser = existingUser.get();

        if (!dbUser.getPassword().equals(user.getPassword())) {
            model.addAttribute("loginError", " Incorrect password. Please try again.");
            return "signin";
        }

        //SAVE USER IN SESSION 
        session.setAttribute("loggedInUser", dbUser);
        session.setAttribute("customerId", dbUser.getId());

        // redirect to home or customer page
        return "redirect:/home";
    }


 //  Forgot Password Page
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

    //  Home page
    @GetMapping("/home")
    public String homePage() {
        return "home";
    }
    

    @GetMapping("/about")
    public String aboutPage() {
        return "about";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "contact";
    }

    @GetMapping("/our-story")
    public String ourStoryPage() {
        return "our-story";
    }

    @GetMapping("/faq")
    public String faqPage() {
        return "faq";
    }

    @GetMapping("gallery")
    public String galleryPage() {
        return "gallery";
    }
    
    @GetMapping("/signout")
    public String signout(HttpSession session) {
        session.invalidate();        
        return "signout"; 
    }

}
