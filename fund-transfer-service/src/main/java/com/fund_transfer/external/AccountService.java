package com.fund_transfer.external;

import com.fund_transfer.configuration.FeignClientConfiguration;
import com.fund_transfer.model.dto.AccountDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.fund_transfer.model.dto.response.Response;

@FeignClient(name = "account-service", configuration = FeignClientConfiguration.class)
public interface AccountService {

    /**
     * Retrieves an account by account number.
     *
     * @param accountNumber The account number.
     * @return The account matching the account number.
     */
    @GetMapping("/api/accounts")
    ResponseEntity<AccountDto> readByAccountNumber(@RequestParam String accountNumber);

    /**
     * Update an accountDto with the given accountDto number.
     *
     * @param accountNumber The accountDto number of the accountDto to be updated.
     * @param accountDto The updated accountDto information.
     * @return The response entity containing the response.
     */
    @PutMapping("/api/accounts")
    ResponseEntity<Response> updateAccount(@RequestParam String accountNumber, @RequestBody AccountDto accountDto);
}
