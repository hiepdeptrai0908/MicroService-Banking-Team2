package com.fund_transfer.external;

import com.fund_transfer.configuration.FeignClientConfiguration;
import com.fund_transfer.model.dto.Account;
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
     * Lấy thông tin tài khoản theo số tài khoản.
     *
     * @param accountNumber Số tài khoản cần tìm.
     * @return Tài khoản tương ứng với số tài khoản.
     */
    @GetMapping("/accounts")
    ResponseEntity<Account> readByAccountNumber(@RequestParam String accountNumber);

    /**
     * Cập nhật thông tin tài khoản với số tài khoản đã cho.
     *
     * @param accountNumber Số tài khoản của tài khoản cần cập nhật.
     * @param account Thông tin tài khoản đã cập nhật.
     * @return Đối tượng phản hồi chứa thông tin phản hồi.
     */
    @PutMapping("/accounts")
    ResponseEntity<Response> updateAccount(@RequestParam String accountNumber, @RequestBody Account account);
}
