package com.user_service.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

    public class JwtTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");
        try {
            // Phân tích token và lấy thông tin người dùng
            String username = Jwts.parser()
                    .setSigningKey("team2-secretKey") // Khoá bí mật
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();

            if (username != null) {
                // Tạo đối tượng xác thực
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (ExpiredJwtException e) {
            // Xử lý token hết hạn
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write("Token đã hết hạn!"); // Gửi thông báo hết hạn
            return; // Ngăn chuyển tiếp yêu cầu
        } catch (SignatureException e) {
            // Xử lý token không hợp lệ
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write("Token không hợp lệ!"); // Gửi thông báo không hợp lệ
            return; // Ngăn chuyển tiếp yêu cầu
        } catch (Exception e) {
            // Xử lý các lỗi khác
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write("Đã xảy ra lỗi với token!"); // Gửi thông báo lỗi chung
            return; // Ngăn chuyển tiếp yêu cầu
        }

        // Chuyển tiếp yêu cầu
        filterChain.doFilter(request, response);
    }
}

