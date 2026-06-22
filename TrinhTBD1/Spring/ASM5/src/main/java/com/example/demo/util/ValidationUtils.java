package com.example.demo.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

public class ValidationUtils {

    private static final Validator validator;

    static {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    private ValidationUtils() {
    }

    public static <T> void validate(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Object to validate cannot be null");
        }
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (!violations.isEmpty()) {
            ConstraintViolation<T> violation = violations.iterator().next();
            throw new IllegalArgumentException(new StringBuilder(violation.getPropertyPath().toString())
                    .append(" ")
                    .append(violation.getMessage())
                    .toString());
        }
    }
}
