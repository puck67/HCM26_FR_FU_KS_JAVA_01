package com.lms.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        // Log the exception details
        System.err.println("GLOBAL EXCEPTION CAUGHT: " + ex.getMessage());
        ex.printStackTrace();

        model.addAttribute("activePage", "home");
        model.addAttribute("content", "error :: body");
        model.addAttribute("errorMessage", ex.getMessage());
        return "layout";
    }
}
