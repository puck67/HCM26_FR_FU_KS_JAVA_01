package com.lms.assessment.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Global exception handler scoped ONLY to classes annotated with {@link Controller}.
 *
 * <p>By using {@code annotations = Controller.class} we explicitly exclude
 * {@code @RestController} / {@code ResponseEntity} endpoints from this advice.
 * Those endpoints (e.g. {@code downloadFile}) MUST handle their own exceptions
 * locally, because a redirect-String return type cannot be written into a
 * {@code ResponseEntity} response — doing so would throw an
 * {@code IllegalStateException} at runtime.
 */
@Slf4j
@ControllerAdvice(annotations = Controller.class)
public class GlobalExceptionHandler {

    /**
     * Catches any unhandled {@link StorageException} that escapes a {@code @Controller}
     * method and converts it to a user-visible flash error message.
     */
    @ExceptionHandler(StorageException.class)
    public String handleStorageException(
            StorageException ex,
            RedirectAttributes redirectAttributes) {

        log.error("StorageException caught by GlobalExceptionHandler: {}", ex.getMessage(), ex);
        redirectAttributes.addFlashAttribute("errorMessage",
                "Storage error: " + ex.getMessage());

        return "redirect:/assessments";
    }

    /**
     * Safety net: catches any other unexpected {@link RuntimeException} from a
     * {@code @Controller} method to prevent a bare 500 white-label error page.
     */
    @ExceptionHandler(RuntimeException.class)
    public String handleUnexpected(
            RuntimeException ex,
            RedirectAttributes redirectAttributes) {

        log.error("Unexpected error in controller: {}", ex.getMessage(), ex);
        redirectAttributes.addFlashAttribute("errorMessage",
                "An unexpected error occurred. Please try again.");

        return "redirect:/assessments";
    }
}
