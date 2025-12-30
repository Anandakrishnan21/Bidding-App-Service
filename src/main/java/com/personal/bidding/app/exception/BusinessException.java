package com.personal.bidding.app.exception;

public class BusinessException extends RuntimeException{
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
