package com.account_service.external;

import com.account_service.config.FeignConfiguration;
import com.account_service.model.dto.external.UserDto;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "user-service", configuration = FeignConfiguration.class)
public interface UserService {

    /**
     * Read user by id response entity.
     *
     * @param userId the user id
     * @return the response entity
     */
    @GetMapping("/api/users/{userId}")
    ResponseEntity<UserDto> readUserById(@PathVariable Long userId);
}