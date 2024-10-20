package com.user_service.service;

import com.user_service.dto.JwtResponse;
import com.user_service.dto.MessageResponse;
import com.user_service.entity.User;
import com.user_service.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;

    public MessageResponse registerUser(User user) {
        MessageResponse message = new MessageResponse();
        // Kiểm tra xem tên người dùng đã tồn tại hay chưa
        if (userRepository.existsByUsername(user.getUsername())) {
            message.setMessage("username đã tồn tại!");
            return message;
        }

        // Mã hóa mật khẩu trước khi lưu vào database (sử dụng BCrypt)
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        message.setMessage("Đăng ký thành công.");
        message.setUser(savedUser);

        return message;
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public JwtResponse loginUser(String username, String password) {
        User user = findByUsername(username);
        if (user != null && new BCryptPasswordEncoder().matches(password, user.getPassword())) {
            // Tạo JWT token
            String token = Jwts.builder()
                    .setSubject(user.getUsername())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 2)) // 2 phút
                    .signWith(SignatureAlgorithm.HS512, "team2-secretKey")
                    .compact();
            return new JwtResponse(token);
        }
        return null; // Hoặc có thể ném ngoại lệ tùy vào cách bạn muốn xử lý
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findAll();
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}

