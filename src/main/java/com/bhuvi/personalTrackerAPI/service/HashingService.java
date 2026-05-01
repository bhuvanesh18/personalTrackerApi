package com.bhuvi.personalTrackerAPI.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class HashingService {

    @Value("${app.security.salt}")
    private String salt;

    public String hashPassword(String password) {
        return BCrypt.hashpw(password + salt,  BCrypt.gensalt());
    }

    public boolean validatePassword(String rawPassword, String hashedPassword) {
        return BCrypt.checkpw(rawPassword + salt, hashedPassword);
    }

}
