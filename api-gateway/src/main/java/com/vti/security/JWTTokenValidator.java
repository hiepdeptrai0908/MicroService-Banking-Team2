package com.vti.security;

import com.vti.utils.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.stereotype.Component;

@Component
public class JWTTokenValidator {
    public boolean validateToken(String token) {
        try {
            // Giải mã và kiểm tra tính hợp lệ của token
            Claims claims = Jwts.parser()
                    .setSigningKey(Constants.SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            // Có thể kiểm tra thêm các thuộc tính khác của token nếu cần
            return true;
        } catch (SignatureException e) {
            // Nếu giải mã thất bại, token không hợp lệ
            return false;
        }
    }
}
