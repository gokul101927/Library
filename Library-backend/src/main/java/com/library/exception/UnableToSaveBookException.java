package com.library.exception;

public class UnableToSaveBookException extends RuntimeException {
    public UnableToSaveBookException(String message) {
        super(message);
    }
}
