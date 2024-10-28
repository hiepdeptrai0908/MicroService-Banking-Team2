package com.fund_transfer.exception;

public class UnauthorizedAccessException extends GlobalException{

        public UnauthorizedAccessException(String errorMessage) {
            super(GlobalErrorCode.FORBIDDEN, errorMessage);
        }
}
