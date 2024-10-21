package com.account_service.utils;

import com.account_service.exception.UnauthorizedException;
import com.account_service.model.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public class JwtUtil {// You can externalize this key to a config file

    public Long extractClaimsFromJwt(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header is missing or invalid");
        }

        String token = header.replace("Bearer ", "");

        Claims claims = Jwts.parser()
                .setSigningKey(Constants.SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", Long.class);
    }
}
