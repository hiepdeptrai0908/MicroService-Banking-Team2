package com.fund_transfer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fund_transfer.model.dto.FundTransferDto;
import com.fund_transfer.model.dto.request.FundTransferRequest;
import com.fund_transfer.model.dto.response.FundTransferResponse;
import com.fund_transfer.service.FundTransferService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fund-transfers")
@CrossOrigin
public class FundTransferController {

    private final FundTransferService fundTransferService;

    /**
     * Xử lý yêu cầu chuyển tiền.
     *
     * @param fundTransferRequest Đối tượng yêu cầu chuyển tiền.
     * @return Đối tượng phản hồi chứa phản hồi chuyển tiền.
     */
    @PostMapping
    public ResponseEntity<FundTransferResponse> fundTransfer(@RequestBody FundTransferRequest fundTransferRequest) {
        return new ResponseEntity<>(fundTransferService.fundTransfer(fundTransferRequest), HttpStatus.CREATED);
    }

    /**
     * Lấy chi tiết chuyển tiền từ ID tham chiếu đã cho.
     *
     * @param referenceId ID tham chiếu của giao dịch chuyển tiền.
     * @return Chi tiết giao dịch chuyển tiền dưới dạng ResponseEntity.
     */
    @GetMapping("/{referenceId}")
    public ResponseEntity<FundTransferDto> getTransferDetailsFromReferenceId(@PathVariable String referenceId) {
        return new ResponseEntity<>(fundTransferService.getTransferDetailsFromReferenceId(referenceId), HttpStatus.OK);
    }

    /**
     * Lấy tất cả các giao dịch chuyển tiền theo ID tài khoản.
     *
     * @param accountId ID của tài khoản.
     * @return Danh sách các DTO chuyển tiền.
     */
    @GetMapping
    public ResponseEntity<List<FundTransferDto>> getAllTransfersByAccountId(@RequestParam String accountId) {
        return new ResponseEntity<>(fundTransferService.getAllTransfersByAccountId(accountId), HttpStatus.OK);
    }
}
