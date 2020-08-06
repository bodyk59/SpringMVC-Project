package com.softserve.academy.exception;

public class MarathonInnerException extends RuntimeException {
    public MarathonInnerException() {}

    public MarathonInnerException(String message) {
        super(message);
    }
}
