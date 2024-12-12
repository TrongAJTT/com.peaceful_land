package com.example.peaceful_land.Service.security;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void storeOtp(String email, String otp, long ttlInSeconds) {
        redisTemplate.opsForValue().set(email, otp, ttlInSeconds, TimeUnit.SECONDS);
    }

    public boolean verifyOtp(String email, String otp) {
        String storedOtp = redisTemplate.opsForValue().get(email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            redisTemplate.delete(email); // Xóa OTP sau khi xác thực
            return true;
        }
        return false;
    }
}
