package com.example.ex2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private boolean isApiRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri != null && uri.startsWith("/api");
    }

    private String getFormattedTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    @ExceptionHandler(Exception.class)
    public Object handleAllExceptions(Exception ex, HttpServletRequest request) {
        ex.printStackTrace();

        if (isApiRequest(request)) {
            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", getFormattedTimestamp());
            body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            body.put("error", "Internal Server Error");
            body.put("message", ex.getMessage());
            body.put("exception", ex.getClass().getName());
            body.put("path", request.getRequestURI());
            
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            body.put("trace", sw.toString());
            
            return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("timestamp", getFormattedTimestamp());
            mav.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            mav.addObject("error", "Internal Server Error");
            mav.addObject("message", ex.getMessage() != null ? ex.getMessage() : "Đã xảy ra lỗi hệ thống không xác định.");
            mav.addObject("exception", ex.getClass().getName());
            mav.addObject("path", request.getRequestURI());
            
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            mav.addObject("trace", sw.toString());
            
            return mav;
        }
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handleNotFoundException(NoHandlerFoundException ex, HttpServletRequest request) {
        if (isApiRequest(request)) {
            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", getFormattedTimestamp());
            body.put("status", HttpStatus.NOT_FOUND.value());
            body.put("error", "Not Found");
            body.put("message", ex.getMessage());
            body.put("path", request.getRequestURI());
            return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
        } else {
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("timestamp", getFormattedTimestamp());
            mav.addObject("status", HttpStatus.NOT_FOUND.value());
            mav.addObject("error", "Not Found");
            mav.addObject("message", "Đường dẫn bạn yêu cầu không tồn tại hoặc đã bị xóa.");
            mav.addObject("exception", ex.getClass().getName());
            mav.addObject("path", request.getRequestURI());
            return mav;
        }
    }
}
