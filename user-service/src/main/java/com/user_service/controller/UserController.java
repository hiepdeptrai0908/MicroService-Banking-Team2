package com.user_service.controller;

import com.user_service.entity.User;
import com.user_service.model.UserLoginRequest;
import com.user_service.dto.JwtResponse;
import com.user_service.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8082")
@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Gọi phương thức registerUser từ UserService và trả về kết quả
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody UserLoginRequest loginRequest) {
        JwtResponse jwtResponse = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
        if (jwtResponse != null) {
            return ResponseEntity.ok(jwtResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username hoặc mật khẩu không đúng");
    }

    @GetMapping
    public List<User> findAllUser() {
        return userService.findAllUser();
    }

    @GetMapping("/{userId}")
    public User findUserById(@PathVariable Long userId) {
        return userService.findById(userId);
    }
}
