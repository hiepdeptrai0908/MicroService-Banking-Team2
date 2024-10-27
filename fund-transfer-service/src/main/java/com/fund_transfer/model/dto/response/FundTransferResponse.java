package com.fund_transfer.model.dto.response;

import com.fund_transfer.model.entity.FundTransfer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundTransferResponse {

    private String transactionId;

    private String message;

    private FundTransfer fundTransfer;
}
