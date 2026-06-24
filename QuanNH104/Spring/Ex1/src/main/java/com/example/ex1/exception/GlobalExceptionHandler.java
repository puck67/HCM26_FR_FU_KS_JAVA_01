package com.example.ex1.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Helper method to check if the request is an API request (starts with /api)
    private boolean isApiRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri != null && uri.startsWith("/api");
    }

    // Generic Exception handler
    @ExceptionHandler(Exception.class)
    public Object handleAllExceptions(Exception ex, HttpServletRequest request) {
        // Print stack trace in console for debugging
        ex.printStackTrace();

        if (isApiRequest(request)) {
            // Return JSON response for API endpoints
            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            body.put("error", "Internal Server Error");
            body.put("message", ex.getMessage());
            body.put("exception", ex.getClass().getName());
            body.put("path", request.getRequestURI());
            
            // Get stack trace
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            body.put("trace", sw.toString());
            
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            // Return ModelAndView rendering the custom error page for web UI
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("timestamp", LocalDateTime.now());
            mav.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            mav.addObject("error", "Internal Server Error");
            mav.addObject("message", ex.getMessage() != null ? ex.getMessage() : "Đã xảy ra lỗi hệ thống không xác định.");
            mav.addObject("exception", ex.getClass().getName());
            mav.addObject("path", request.getRequestURI());
            
            // Get stack trace for easy debug on error page
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            mav.addObject("trace", sw.toString());
            
            return mav;
        }
    }

    // Specific handler for 404 Not Found Exception
    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handleNotFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", LocalDateTime.now());
            body.put("status", HttpStatus.NOT_FOUND.value());
            body.put("error", "Not Found");
            body.put("message", ex.getMessage());
            body.put("path", request.getRequestURI());
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } else {
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("timestamp", LocalDateTime.now());
            mav.addObject("status", HttpStatus.NOT_FOUND.value());
            mav.addObject("error", "Not Found");
            mav.addObject("message", "Đường dẫn bạn yêu cầu không tồn tại hoặc đã bị xóa.");
            mav.addObject("exception", ex.getClass().getName());
            mav.addObject("path", request.getRequestURI());
            return mav;
        }
    }
}
