package com.user_service.modal;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String username;
    private String password;
}
