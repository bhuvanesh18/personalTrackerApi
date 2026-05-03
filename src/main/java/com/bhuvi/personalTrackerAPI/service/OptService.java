package com.bhuvi.personalTrackerAPI.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OptService {

    private final StringRedisTemplate redisTemplate;

    private static final String OTP_PREFIX = "PT-OTP-";

    public String generateOTP(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    // redis cache
    // 1. Save OTP with a 5-minute expiration
    public void saveOtp(String mailId, String otp) {
        redisTemplate.opsForValue().set(
                OTP_PREFIX + mailId,
                otp,
                5,
                TimeUnit.MINUTES
        );
    }

    // 2. Verify OTP
    public boolean verifyOtp(String mailId, String userOtp) {
        String serverOtp = redisTemplate.opsForValue().get(OTP_PREFIX + mailId);
        return userOtp.equals(serverOtp);
    }

    // 3. Delete OTP after successful verification
    public void clearOtp(String mailId) {
        redisTemplate.delete(OTP_PREFIX + mailId);
    }

}
