package org.example.util;

import java.util.regex.Pattern;

public class InputValidator {
    // Basic email regex pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    // Vietnamese phone number (10 digits starting  0, e.g. 0912345678, 03xxxxxxxx, 08xxxxxxxx, 07xxxxxxxx)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(0[35789])([0-9]{8})$");

    private static final Pattern STUDENT_CODE_PATTERN = Pattern.compile("^SV([0-9]{3})$", Pattern.CASE_INSENSITIVE);

    private static final Pattern SUBJECT_CODE_PATTERN = Pattern.compile("^MH\\d+$", Pattern.CASE_INSENSITIVE);

    private static final Pattern NAME_PATTERN = Pattern.compile(
            "^[a-zA-Z\\s]{1,30}$"
    );

    public static boolean isValidStudentCode(String code) {
        return code != null && STUDENT_CODE_PATTERN.matcher(code.trim()).matches();
    }

    public static boolean isValidSubjectCode(String code) {
        return code != null && SUBJECT_CODE_PATTERN.matcher(code.trim()).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && NAME_PATTERN.matcher(name.trim()).matches();
    }

    public static boolean isValidCredits(int credits) {
        return credits > 0 && credits <= 12;
    }

    public static boolean isValidScore(double score) {
        return score >= 0.0 && score <= 10.0;
    }
}
