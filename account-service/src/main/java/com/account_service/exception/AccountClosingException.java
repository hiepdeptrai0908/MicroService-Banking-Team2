package com.account_service.exception;

public class AccountClosingException extends GlobalException{

    public AccountClosingException(String errorMessage) {
        super(GlobalErrorCode.BAD_REQUEST, errorMessage);
    }
}
