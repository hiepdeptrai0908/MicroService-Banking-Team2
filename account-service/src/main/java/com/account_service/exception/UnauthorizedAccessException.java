package com.account_service.exception;

public class UnauthorizedAccessException extends GlobalException{

        public UnauthorizedAccessException(String errorMessage) {
            super(GlobalErrorCode.FORBIDDEN, errorMessage);
        }
}
