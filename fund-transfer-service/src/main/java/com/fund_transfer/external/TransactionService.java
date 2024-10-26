package com.fund_transfer.external;

import com.fund_transfer.configuration.FeignClientConfiguration;
import com.fund_transfer.model.dto.Transaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.fund_transfer.model.dto.response.Response;

import java.util.List;

@FeignClient(name = "transactions-service", configuration = FeignClientConfiguration.class)
public interface TransactionService {

    /**
     * Điểm cuối để thực hiện giao dịch.
     *
     * @param transaction Đối tượng giao dịch chứa thông tin chi tiết của giao dịch.
     * @return Đối tượng ResponseEntity chứa phản hồi của giao dịch.
     */
    @PostMapping("/transactions")
    ResponseEntity<Response> makeTransaction(@RequestBody Transaction transaction);

    /**
     * Thực hiện các giao dịch nội bộ.
     *
     * @param transactions         Danh sách các giao dịch cần được xử lý.
     * @param transactionReference Tham chiếu cho giao dịch.
     * @return Đối tượng phản hồi chứa thông tin phản hồi.
     */
    @PostMapping("/transactions/internal")
    ResponseEntity<Response> makeInternalTransactions(@RequestBody List<Transaction> transactions, @RequestParam String transactionReference);
}
