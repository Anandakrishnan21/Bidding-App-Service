package com.personal.bidding.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(AuctionExistsException.class)
    public ResponseEntity<Map<String, Object>> handleAuctionExistsException(AuctionExistsException exception) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("message", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    @ExceptionHandler(TeamNameExistsException.class)
    public ResponseEntity<Map<String, Object>> handleTeamNameExistsException(TeamNameExistsException exception) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("message", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoDataFoundException(NoDataFoundException exception) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("message", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    @ExceptionHandler(EmptyFieldException.class)
    public ResponseEntity<Map<String, Object>> handleNoDataFoundException(EmptyFieldException exception) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("message", exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
