package com.example.peaceful_land.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@WebFilter("/*")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    // Constructor Injection của JwtTokenProvider
    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Lấy token từ header "Authorization"
        String token = jwtTokenProvider.resolveToken(request);

        // Nếu token tồn tại và hợp lệ
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // Lấy username từ token
            String username = jwtTokenProvider.getUsername(token);

            // Nếu username hợp lệ (không null hoặc trống)
            if (username != null) {
                // Tạo đối tượng Authentication với thông tin từ token
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, null);

                // Cung cấp chi tiết về yêu cầu (có thể là quyền hạn, vai trò nếu cần)
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Đặt đối tượng Authentication vào SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Tiếp tục xử lý chuỗi filter (cho các bộ lọc khác trong Spring Security)
        filterChain.doFilter(request, response);
    }
}
