package com.personal.bidding.app.exception;

public class TeamNameExistsException extends RuntimeException {
    public TeamNameExistsException(String message) {
        super(message);
    }
}
