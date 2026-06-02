package com.lms.assessment.exception;

/**
 * Custom unchecked exception for all file-storage related failures.
 * Centralises error propagation — any storage layer error bubbles up as this type,
 * which the @ControllerAdvice can catch and convert to a user-facing flash message.
 */
public class StorageException extends RuntimeException {

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
