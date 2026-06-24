package com.lms.materialmanager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.FileNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body("Maximum file size is 10MB.");
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<String> handleFileNotFoundException(FileNotFoundException exc) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Requested file does not exist.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exc) {
        // We throw IllegalArgumentException for various validation failures (e.g. Unsupported file format.)
        String message = exc.getMessage();
        if ("Unsupported file format.".equals(message)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if ("Maximum file size is 10MB.".equals(message)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if ("Requested file does not exist.".equals(message)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
