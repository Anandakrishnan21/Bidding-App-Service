package com.personal.bidding.app.exception;

public class AuctionExistsException extends RuntimeException{

    public AuctionExistsException(String message){
        super(message);
    }
}
