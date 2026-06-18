package com.example.demo.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, RedirectAttributes redirectAttributes) {
        StringBuilder sb = new StringBuilder();
        sb.append("An unexpected error occurred: ").append(ex.getMessage());
        log.error(sb.toString(), ex);
        redirectAttributes.addFlashAttribute("errorMessage", sb.toString());
        return "redirect:/menus";
    }
}
