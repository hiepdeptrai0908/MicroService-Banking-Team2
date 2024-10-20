package com.account_service.service.implementation;

import com.account_service.exception.*;
import com.account_service.external.SequenceService;
import com.account_service.external.TransactionService;
import com.account_service.external.UserService;
import com.account_service.model.AccountStatus;
import com.account_service.model.AccountType;
import com.account_service.model.dto.AccountDto;
import com.account_service.model.dto.AccountStatusUpdate;
import com.account_service.model.dto.external.TransactionResponse;
import com.account_service.model.dto.external.UserDto;
import com.account_service.model.dto.response.Response;
import com.account_service.model.entity.Account;
import com.account_service.model.mapper.AccountMapper;
import com.account_service.repository.AccountRepository;
import com.account_service.service.AccountService;
import com.account_service.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static com.account_service.model.Constants.ACC_PREFIX;


@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;
    private final SequenceService sequenceService;
    private final TransactionService transactionService;

    private final AccountMapper accountMapper = new AccountMapper();

    private final JwtUtil jwtUtil = new JwtUtil();

    @Value("${spring.application.ok}")
    private String success;

    @Override
    public Response createAccount(AccountDto accountDto, HttpServletRequest request) {

        Long userId = jwtUtil.extractClaimsFromJwt(request);

        accountDto.setUserId(userId);

        ResponseEntity<UserDto> user;
        try {
            // FeignClient call to get user by ID
            user = userService.readUserById(userId);
        } catch (FeignException.NotFound e) {
            // Handle 404 - Not Found from the user service
            throw new ResourceNotFound("User not found on the server");
        } catch (FeignException e) {
            // Handle other FeignClient-related exceptions
            log.error("Error during FeignClient call: " + e.getMessage(), e);
            throw new ServiceException("An error occurred while communicating with the user service");
        }

        if (Objects.isNull(user.getBody())) {
            throw new ResourceNotFound("User not found on the server");
        }

        accountRepository.findAccountByUserIdAndAccountType(accountDto.getUserId(), AccountType.valueOf(accountDto.getAccountType()))
                .ifPresent(account -> {
                    log.error("Account already exists on the server");
                    throw new ResourceConflict("Account already exists on the server");
                });

        Account account = accountMapper.convertToEntity(accountDto);
        account.setAccountNumber(ACC_PREFIX + String.format("%07d",sequenceService.generateAccountNumber().getAccountNumber()));
        account.setAccountStatus(AccountStatus.PENDING);
        account.setAvailableBalance(BigDecimal.valueOf(0));
        account.setAccountType(AccountType.valueOf(accountDto.getAccountType()));
        accountRepository.save(account);
        return Response.builder()
                .responseCode(success)
                .message(" Account created successfully").build();
    }

    @Override
    public Response updateStatus(String accountNumber, AccountStatusUpdate accountUpdate) {

        return accountRepository.findAccountByAccountNumber(accountNumber)
                .map(account -> {
                    if(account.getAccountStatus().equals(AccountStatus.ACTIVE)){
                        throw new AccountStatusException("Account is inactive/closed");
                    }
                    if(account.getAvailableBalance().compareTo(BigDecimal.ZERO) < 0 || account.getAvailableBalance().compareTo(BigDecimal.valueOf(1000)) < 0){
                        throw new InSufficientFunds("Minimum balance of Rs.1000 is required");
                    }
                    account.setAccountStatus(accountUpdate.getAccountStatus());
                    accountRepository.save(account);
                    return Response.builder().message("Account updated successfully").responseCode(success).build();
                }).orElseThrow(() -> new ResourceNotFound("Account not on the server"));

    }

    @Override
    public AccountDto readAccountByAccountNumber(String accountNumber) {

        return accountRepository.findAccountByAccountNumber(accountNumber)
                .map(account -> {
                    AccountDto accountDto = accountMapper.convertToDto(account);
                    accountDto.setAccountType(account.getAccountType().toString());
                    accountDto.setAccountStatus(account.getAccountStatus().toString());
                    return accountDto;
                })
                .orElseThrow(ResourceNotFound::new);
    }

    @Override
    public Response updateAccount(String accountNumber, AccountDto accountDto) {

        return accountRepository.findAccountByAccountNumber(accountDto.getAccountNumber())
                .map(account -> {
                    BeanUtils.copyProperties(accountDto, account);
                    accountRepository.save(account);
                    return Response.builder()
                            .responseCode(success)
                            .message("Account updated successfully").build();
                }).orElseThrow(() -> new ResourceNotFound("Account not found on the server"));
    }

    @Override
    public String getBalance(String accountNumber) {

        return accountRepository.findAccountByAccountNumber(accountNumber)
                .map(account -> account.getAvailableBalance().toString())
                .orElseThrow(ResourceNotFound::new);
    }

    @Override
    public List<TransactionResponse> getTransactionsFromAccountId(String accountId) {
        return transactionService.getTransactionsFromAccountId(accountId);
    }

    @Override
    public Response closeAccount(String accountNumber) {

        return accountRepository.findAccountByAccountNumber(accountNumber)
                .map(account -> {
                    if(BigDecimal.valueOf(Double.parseDouble(getBalance(accountNumber))).compareTo(BigDecimal.ZERO) != 0) {
                        throw new AccountClosingException("Balance should be zero");
                    }
                    account.setAccountStatus(AccountStatus.CLOSED);
                    return Response.builder()
                            .message("Account closed successfully").message(success)
                            .build();
                }).orElseThrow(ResourceNotFound::new);

    }

    @Override
    public AccountDto readAccountByUserId(Long userId) {

        return accountRepository.findAccountByUserId(userId)
                .map(account ->{
                    if(!account.getAccountStatus().equals(AccountStatus.ACTIVE)){
                        throw new AccountStatusException("Account is inactive/closed");
                    }
                    AccountDto accountDto = accountMapper.convertToDto(account);
                    accountDto.setAccountStatus(account.getAccountStatus().toString());
                    accountDto.setAccountType(account.getAccountType().toString());
                    return accountDto;
                }).orElseThrow(ResourceNotFound::new);
    }
}