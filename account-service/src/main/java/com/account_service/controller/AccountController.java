package com.account_service.controller;

import com.account_service.model.dto.AccountDto;
import com.account_service.model.dto.AccountStatusUpdate;
import com.account_service.model.dto.external.TransactionResponse;
import com.account_service.model.dto.response.AccountResponse;
import com.account_service.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accounts")
@CrossOrigin
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountDto accountDto, HttpServletRequest request) {
        return new ResponseEntity<>(accountService.createAccount(accountDto, request), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<AccountResponse> updateAccountStatus(@RequestParam String accountNumber, @RequestBody AccountStatusUpdate accountStatusUpdate, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.updateStatus(accountNumber, accountStatusUpdate, request));
    }

    @GetMapping
    public ResponseEntity<AccountDto> readByAccountNumber(@RequestParam String accountNumber, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.readAccountByAccountNumber(accountNumber, request));
    }

    @PutMapping
    public ResponseEntity<AccountResponse> updateAccount(@RequestParam String accountNumber, @RequestBody AccountDto accountDto, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.updateAccount(accountNumber, accountDto, request));
    }

    @GetMapping("/balance")
    public ResponseEntity<String> accountBalance(@RequestParam String accountNumber, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.getBalance(accountNumber, request));
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactionsFromAccountId(@PathVariable String accountId, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.getTransactionsFromAccountId(accountId, request));
    }

    @PutMapping("/closure")
    public ResponseEntity<AccountResponse> closeAccount(@RequestParam String accountNumber, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.closeAccount(accountNumber, request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<AccountDto>> readAccountsByUserId(@PathVariable Long userId, HttpServletRequest request) {
        return ResponseEntity.ok(accountService.readAccountsByUserId(userId, request));
    }
}
