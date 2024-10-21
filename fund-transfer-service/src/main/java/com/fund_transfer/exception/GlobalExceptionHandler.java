package com.fund_transfer.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Value("${spring.application.bad_request}")
    private String badRequest;

    /**
     * Xử lý ngoại lệ xác thực tham số phương thức.
     *
     * @param ex      Ngoại lệ MethodArgumentNotValidException cần xử lý.
     * @param headers Các HttpHeaders cần bao gồm trong phản hồi.
     * @param status  HttpStatus được thiết lập trong phản hồi.
     * @param request WebRequest liên quan đến yêu cầu.
     * @return Một ResponseEntity chứa ErrorResponse và HttpStatus.BAD_REQUEST.
     */
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return new ResponseEntity<>(new ErrorResponse(badRequest, ex.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * Xử lý GlobalException và trả về một đối tượng ResponseEntity với phản hồi lỗi thích hợp.
     *
     * @param globalException Đối tượng GlobalException cần xử lý.
     * @return Một đối tượng ResponseEntity đại diện cho phản hồi lỗi.
     */
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Object> handleGlobalException(GlobalException globalException) {

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.builder()
                        .errorCode(globalException.getErrorCode())
                        .message(globalException.getMessage())
                        .build());
    }
}
