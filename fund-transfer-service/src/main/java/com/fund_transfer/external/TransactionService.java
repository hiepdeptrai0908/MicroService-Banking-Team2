package com.fund_transfer.external;

import com.fund_transfer.configuration.FeignClientConfiguration;
import com.fund_transfer.model.dto.TransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.fund_transfer.model.dto.response.Response;

import java.util.List;

@FeignClient(name = "transaction-service", configuration = FeignClientConfiguration.class)
public interface TransactionService {

    /**
     * Điểm cuối để thực hiện giao dịch.
     *
     * @param transactionDto Đối tượng giao dịch chứa thông tin chi tiết của giao dịch.
     * @return Đối tượng ResponseEntity chứa phản hồi của giao dịch.
     */
    @PostMapping("/api/transactions")
    ResponseEntity<Response> makeTransaction(@RequestBody TransactionDto transactionDto);

    /**
     * Thực hiện các giao dịch nội bộ.
     *
     * @param transactionDtos         Danh sách các giao dịch cần được xử lý.
     * @param transactionReference Tham chiếu cho giao dịch.
     * @return Đối tượng phản hồi chứa thông tin phản hồi.
     */
    @PostMapping("/api/transactions/internal")
    ResponseEntity<Response> makeInternalTransactions(@RequestBody List<TransactionDto> transactionDtos, @RequestParam("transactionReference") String transactionReference);

}
