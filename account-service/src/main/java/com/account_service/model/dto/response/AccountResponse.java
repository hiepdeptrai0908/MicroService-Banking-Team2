package com.account_service.model.dto.response;

import com.account_service.model.dto.AccountDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {
    private String responseCode;
    private String message;

    private AccountDto accountDto;
}
