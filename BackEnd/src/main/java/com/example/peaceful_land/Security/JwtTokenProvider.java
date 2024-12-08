package com.example.peaceful_land.Security;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // Khóa bí mật cố định, đảm bảo có độ dài đủ (512 bits cho HS512)
    private final String JWT_SECRET = "your_secret_key_for_JWT_your_secret_key_for_JWT";  // Đảm bảo rằng chuỗi này có độ dài ít nhất 512 bit (64 ký tự ASCII)

    private final long JWT_EXPIRATION = 86400000L; // 1 ngày

    // Tạo token JWT
    public String generateToken(String username) {
        // Tạo Key an toàn từ khóa bí mật với độ dài đủ cho HS512 (>= 512 bits)
        Key signingKey = new SecretKeySpec(JWT_SECRET.getBytes(), SignatureAlgorithm.HS512.getJcaName());

        return Jwts.builder()
                .setSubject(username)  // Đặt thông tin người dùng
                .setIssuedAt(new Date())  // Thời gian tạo token
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))  // Thời gian hết hạn token
                .signWith(signingKey)  // Sử dụng khóa bí mật để ký token
                .compact();  // Xây dựng token
    }

    // Giải mã và kiểm tra token
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);  // Lấy token từ header
        }
        return null;
    }

    // Kiểm tra tính hợp lệ của token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(JWT_SECRET.getBytes()).build().parseClaimsJws(token);  // Dùng khóa bí mật cố định để xác thực token
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;  // Nếu token không hợp lệ hoặc hết hạn
        }
    }

    // Lấy tên người dùng từ token
    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(JWT_SECRET.getBytes()).build().parseClaimsJws(token).getBody().getSubject();
    }
}
