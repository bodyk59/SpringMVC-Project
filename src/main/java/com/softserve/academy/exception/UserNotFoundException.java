package com.softserve.academy.exception;

public class UserNotFoundException extends MarathonInnerException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
