package com.user_service.service;

import com.user_service.dto.JwtResponse;
import com.user_service.dto.MessageResponse;
import com.user_service.entity.User;

import java.util.List;

public interface IUserService {
    MessageResponse registerUser(User user);
    User findByUsername(String username);

    JwtResponse loginUser(String username, String password);

    List<User> findAllUser();
}
