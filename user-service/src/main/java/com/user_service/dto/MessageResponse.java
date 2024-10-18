package com.user_service.dto;

import com.user_service.entity.User;
import lombok.Data;

@Data
public class MessageResponse {
    private String message;

    private User user;
}
