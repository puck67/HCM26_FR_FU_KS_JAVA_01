package com.example.demo.exception;

public class MenuHasChildrenException extends RuntimeException {

    public MenuHasChildrenException(String message) {
        super(message);
    }
}
