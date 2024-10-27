package com.transaction_service.model.response;

import com.transaction_service.model.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {

    private String responseCode;

    private String message;

    private List<Transaction> transactions;
}
