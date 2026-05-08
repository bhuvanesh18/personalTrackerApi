package com.bhuvi.personalTrackerAPI.service;

import com.bhuvi.personalTrackerAPI.entity.User;
import com.bhuvi.personalTrackerAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final OptService optService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    public User authenticateUser(User user) {
        User existingUser = userRepository.findByVerifiedMailId(user.getMailId());
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

    public void generateOtpAndMail(String mailId, String subject) {
        final String otp = optService.generateOTP(4);
        System.out.println("Generated a otp");
        System.out.println("Sending OTP to user mail");
        emailService.sendEmail(mailId, subject, otp);
        System.out.println("Sent OTP to user mail");
        System.out.println("Saving OTP to cache");
        optService.saveOtp(mailId, otp);
        System.out.println("Saved OTP to cache");
    }

    public boolean verifyOtp(String mailId, String otp) {
        boolean isValid = optService.verifyOtp(mailId, otp);
        if (isValid) {
            optService.clearOtp(mailId);
        }
        return isValid;
    }


}
