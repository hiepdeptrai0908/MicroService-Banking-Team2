package com.account_service.controller;

import com.account_service.model.dto.AccountDto;
import com.account_service.model.dto.AccountStatusUpdate;
import com.account_service.model.dto.external.TransactionResponse;
import com.account_service.model.dto.response.Response;
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
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Response> createAccount(@RequestBody AccountDto accountDto, HttpServletRequest request) {
        return new ResponseEntity<>(accountService.createAccount(accountDto, request), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<Response> updateAccountStatus(@RequestParam String accountNumber, @RequestBody AccountStatusUpdate accountStatusUpdate) {
        return ResponseEntity.ok(accountService.updateStatus(accountNumber, accountStatusUpdate));
    }

    @GetMapping
    public ResponseEntity<AccountDto> readByAccountNumber(@RequestParam String accountNumber) {
        return ResponseEntity.ok(accountService.readAccountByAccountNumber(accountNumber));
    }

    @PutMapping
    public ResponseEntity<Response> updateAccount(@RequestParam String accountNumber, @RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(accountService.updateAccount(accountNumber, accountDto));
    }

    @GetMapping("/balance")
    public ResponseEntity<String> accountBalance(@RequestParam String accountNumber) {
        return ResponseEntity.ok(accountService.getBalance(accountNumber));
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactionsFromAccountId(@PathVariable String accountId) {
        return ResponseEntity.ok(accountService.getTransactionsFromAccountId(accountId));
    }

    @PutMapping("/closure")
    public ResponseEntity<Response> closeAccount(@RequestParam String accountNumber) {
        return ResponseEntity.ok(accountService.closeAccount(accountNumber));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<AccountDto> readAccountByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(accountService.readAccountByUserId(userId));
    }
}
