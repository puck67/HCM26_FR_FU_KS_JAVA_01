package org.example.util;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;


public class Validator {


    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\d{9,11}$");

    private static final Pattern ID_PATTERN =
            Pattern.compile("^[A-Za-z0-9]+$");

    
    public static final Predicate<String> NOT_BLANK  =
            s -> s != null && !s.trim().isEmpty();

    public static final Predicate<String> VALID_EMAIL =
            s -> NOT_BLANK.test(s) && EMAIL_PATTERN.matcher(s.trim()).matches();

    public static final Predicate<String> VALID_PHONE =
            s -> NOT_BLANK.test(s) && PHONE_PATTERN.matcher(s.trim()).matches();

    public static final Predicate<String> VALID_ID =
            s -> NOT_BLANK.test(s) && ID_PATTERN.matcher(s.trim()).matches();

    public static final Predicate<Double> VALID_GPA =
            gpa -> gpa >= 0.0 && gpa <= 4.0;

   
    public static boolean isNotBlank(String value)  { return NOT_BLANK.test(value); }
    public static boolean isValidEmail(String email) { return VALID_EMAIL.test(email); }
    public static boolean isValidPhone(String phone) { return VALID_PHONE.test(phone); }
    public static boolean isValidId(String id)       { return VALID_ID.test(id); }
    public static boolean isValidGpa(double gpa)     { return VALID_GPA.test(gpa); }

   
    public static Optional<String> checkField(boolean condition, String errorMessage) {
        return condition ? Optional.empty() : Optional.of(errorMessage);
    }

  
    public static String emailError()               { return "Invalid email. Must match format: user@domain.com"; }
    public static String phoneError()               { return "Invalid phone. Must contain 9-11 digits only."; }
    public static String gpaError()                 { return "Invalid GPA. Must be a number between 0.0 and 4.0."; }
    public static String idError()                  { return "Invalid ID. Must be alphanumeric and non-blank."; }
    public static String blankError(String field)   { return field + " cannot be blank."; }
}
