package com.personal.bidding.app.exception;

public class PlayerExistException extends RuntimeException {
    public PlayerExistException(String message) {
        super(message);
    }
}
