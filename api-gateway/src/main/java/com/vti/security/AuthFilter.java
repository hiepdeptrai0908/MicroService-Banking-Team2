package com.vti.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException; // Thêm import này
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(1) // Thứ tự ưu tiên của filter
@AllArgsConstructor
public class AuthFilter implements GlobalFilter {

    private final JWTTokenValidator jwtTokenValidator; // Lớp xác thực token JWT

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        // Kiểm tra xem URL có phải là /api/users/login hoặc /api/users/register không
        String path = exchange.getRequest().getURI().getPath();
        if (path.equals("/api/users/login") || path.equals("/api/users/register") || path.startsWith("/actuator")) {
            // Nếu là đường dẫn trừ thì không kiểm tra token, tiếp tục chuyển hướng
            return chain.filter(exchange);
        }

        // Kiểm tra xem header Authorization có tồn tại không
        String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            // Nếu không có header Authorization hoặc không đúng định dạng thì trả về thông báo lỗi
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().add("Content-Type", "text/plain; charset=UTF-8");
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap("Xin vui lòng đăng nhập".getBytes())));
        }

        // Lấy token từ header
        String token = authorizationHeader.substring(7);

        try {
            // Kiểm tra tính hợp lệ của token
            if (!jwtTokenValidator.validateToken(token)) {
                // Nếu token không hợp lệ thì trả về thông báo lỗi
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                exchange.getResponse().getHeaders().add("Content-Type", "text/plain; charset=UTF-8");
                return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap("Xin vui lòng đăng nhập".getBytes())));
            }
        } catch (ExpiredJwtException e) {
            // Nếu token đã hết hạn, trả về thông báo lỗi cụ thể
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().add("Content-Type", "text/plain; charset=UTF-8");
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap("Token đã hết hạn. Vui lòng đăng nhập lại.".getBytes())));
        } catch (JwtException e) {
            // Bắt các lỗi khác liên quan đến JWT
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().add("Content-Type", "text/plain; charset=UTF-8");
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap("Token không hợp lệ. Vui lòng đăng nhập lại.".getBytes())));
        }

        // Nếu token hợp lệ, tiếp tục chuyển hướng
        return chain.filter(exchange);
    }
}


