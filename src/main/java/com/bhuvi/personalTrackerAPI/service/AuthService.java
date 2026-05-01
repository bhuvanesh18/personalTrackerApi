package com.bhuvi.personalTrackerAPI.service;

import com.bhuvi.personalTrackerAPI.entity.User;
import com.bhuvi.personalTrackerAPI.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User authenticateUser(User user) {
        User existingUser = userRepository.findByMailId(user.getMailId());
        if(existingUser != null) {
            if (userRepository.validatePassword(user.getPassword(), existingUser.getPassword())) {
                return existingUser;
            } else {
                throw new RuntimeException("Invalid credentials");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

}
