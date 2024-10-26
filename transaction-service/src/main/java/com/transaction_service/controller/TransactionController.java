package com.transaction_service.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.transaction_service.model.dto.TransactionDto;
import com.transaction_service.model.response.Response;
import com.transaction_service.model.response.TransactionRequest;
import com.transaction_service.service.TransactionService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Thêm giao dịch vào hệ thống.
     *
     * @param transactionDto Dữ liệu giao dịch cần thêm.
     * @return Đối tượng phản hồi chứa dữ liệu giao dịch đã thêm.
     */
    @PostMapping
    public ResponseEntity<Response> addTransactions(@RequestBody TransactionDto transactionDto) {
        return new ResponseEntity<>(transactionService.addTransaction(transactionDto), HttpStatus.CREATED);
    }

    /**
     * Xử lý endpoint cho việc thực hiện giao dịch nội bộ.
     * (Một dịch vụ khác đang gọi dịch vụ này để thực hiện giao dịch (fund-transfer-service))
     *
     * @param transactionDtos       Danh sách DTO giao dịch.
     * @param transactionReference  Mã tham chiếu giao dịch.
     * @return                      Đối tượng phản hồi chứa phản hồi.
     */
    @PostMapping("/internal")
    public ResponseEntity<Response> makeInternalTransaction(@RequestBody List<TransactionDto> transactionDtos, @RequestParam String transactionReference) {
        System.out.println("TransactionDto: " + transactionDtos);
        System.out.println("transactionReference: " + transactionReference);
        return new ResponseEntity<>(transactionService.internalTransaction(transactionDtos, transactionReference), HttpStatus.CREATED);
    }

    /**
     * Lấy danh sách giao dịch cho một ID tài khoản nhất định.
     *
     * @param accountId ID của tài khoản.
     * @return Danh sách các giao dịch.
     */
    @GetMapping
    public ResponseEntity<List<TransactionRequest>> getTransactions(@RequestParam String accountId) {
        return new ResponseEntity<>(transactionService.getTransaction(accountId), HttpStatus.OK);
    }

    /**
     * Lấy danh sách yêu cầu giao dịch dựa trên ID tham chiếu giao dịch đã cung cấp.
     *
     * @param referenceId ID tham chiếu giao dịch.
     * @return Một đối tượng ResponseEntity chứa danh sách các yêu cầu giao dịch.
     */
    @GetMapping("/{referenceId}")
    public ResponseEntity<List<TransactionRequest>> getTransactionByTransactionReference(@PathVariable String referenceId) {
        return new ResponseEntity<>(transactionService.getTransactionByTransactionReference(referenceId), HttpStatus.OK);
    }

}
