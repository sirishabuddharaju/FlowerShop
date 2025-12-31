package com.example.demo.serviceimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    // Register User
    @Override
    public void registerUser(User user) {
        userRepository.save(user);
    }

    //Login Check
    @Override
    public Optional<User> loginUser(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    //Check Duplicate Email
    @Override
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void updateUserDetails(String email, User newUserDetails) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User existingUser = userOpt.get();

            // Only update if the new data is not null
            if (newUserDetails.getName() != null && !newUserDetails.getName().isEmpty()) {
                existingUser.setName(newUserDetails.getName());
            }
            if (newUserDetails.getPassword() != null && !newUserDetails.getPassword().isEmpty()) {
                existingUser.setPassword(newUserDetails.getPassword());
                existingUser.setConfirmPassword(newUserDetails.getPassword()); // ✅ to satisfy validation
            }
            if (newUserDetails.getAddress() != null && !newUserDetails.getAddress().isEmpty()) {
                existingUser.setAddress(newUserDetails.getAddress());
            }
            if (newUserDetails.getPhoneNumber() != null && !newUserDetails.getPhoneNumber().isEmpty()) {
                existingUser.setPhoneNumber(newUserDetails.getPhoneNumber());
            }

            // ✅ Ensure confirmPassword is set before saving (prevents validation failure)
            if (existingUser.getConfirmPassword() == null || existingUser.getConfirmPassword().isEmpty()) {
                existingUser.setConfirmPassword(existingUser.getPassword());
            }

            userRepository.save(existingUser);
        }
    }


    }


