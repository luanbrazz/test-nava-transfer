package com.test.luanbraz.navatransfer.exceptions;

import com.test.luanbraz.navatransfer.dto.errors.CustomErrorResponse;

public class CustomException extends RuntimeException {
    private final CustomErrorResponse errorResponse;

    public CustomException(CustomErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
    }

    public CustomErrorResponse getErrorResponse() {
        return errorResponse;
    }
}
