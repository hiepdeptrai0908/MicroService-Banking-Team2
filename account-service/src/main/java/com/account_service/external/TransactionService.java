package com.account_service.external;

import com.account_service.config.FeignConfiguration;
import com.account_service.model.dto.external.TransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "transaction-service", configuration = FeignConfiguration.class)
public interface TransactionService {

    /**
     * Retrieves a list of transactions from the specified account ID.
     *
     * @param accountId the ID of the account
     * @return a list of transaction responses
     */
    @GetMapping("/api/transactions")
    List<TransactionResponse> getTransactionsFromAccountId(@RequestParam String accountId);
}
