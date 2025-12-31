package com.example.demo.service;

import java.util.Optional;
import com.example.demo.entity.User;

public interface UserService {

    void registerUser(User user);

    Optional<User> loginUser(String email, String password);

    boolean emailExists(String email);
    
    Optional<User> findByEmail(String email);  

    public void updateUserDetails(String email, User newUserDetails);

}
