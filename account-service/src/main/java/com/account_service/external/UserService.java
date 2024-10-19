package com.account_service.external;

import com.account_service.model.dto.external.UserDto;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-service")
public interface UserService {

    @GetMapping("/api/users/{userId}")
    ResponseEntity<UserDto> readUserById(@PathVariable Long userId);
}