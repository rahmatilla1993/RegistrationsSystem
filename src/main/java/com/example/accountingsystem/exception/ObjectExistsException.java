package com.example.accountingsystem.exception;

public class ObjectExistsException extends RuntimeException{

    public ObjectExistsException(String message) {
        super(message);
    }
}
