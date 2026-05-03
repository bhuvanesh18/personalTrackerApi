package com.bhuvi.personalTrackerAPI.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void sendOtpMessage(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Personal Tracker APP: Your OTP Verification Code");
        message.setText("Your otp is: " + otp + ". It expires in 5 minutes.");
        mailSender.send(message);
    }
}