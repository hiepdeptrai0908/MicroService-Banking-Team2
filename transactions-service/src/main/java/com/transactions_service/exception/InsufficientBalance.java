package com.transactions_service.exception;

public class InsufficientBalance extends GlobalException {

    public InsufficientBalance(String message) {
        super(message, GlobalErrorCode.BAD_REQUEST);
    }
}
