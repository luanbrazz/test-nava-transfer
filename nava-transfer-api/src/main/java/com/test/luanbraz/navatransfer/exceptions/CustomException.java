package com.test.luanbraz.navatransfer.exceptions;

import com.test.luanbraz.navatransfer.dto.errors.CustomErrorResponse;
import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {
    private final CustomErrorResponse errorResponse;
    private final HttpStatus status;

    public CustomException(CustomErrorResponse errorResponse, HttpStatus status) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
        this.status = status;
    }

    public CustomErrorResponse getErrorResponse() {
        return errorResponse;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
