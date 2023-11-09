package com.mavs.demopark.exception;

public class UsernameUniqueViolationException extends RuntimeException {
    public UsernameUniqueViolationException(String message){
        super(message);
    }
}
