package com.example.util;

public class ValidateUtil {

    public static boolean isValidId(int id) {
        return id > 0;
    }

    public static boolean isValidAge(int age) {
        return age >= 5 && age <= 100;
    }

    public static boolean isValidCredit(int credit) {
        return credit >= 1 && credit <= 10;
    }

    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        String trimmed = name.trim();
        return trimmed.length() >= 2 && trimmed.length() <= 50 && trimmed.matches("^[a-zA-Z\\s\\p{L}]+$");
    }

    public static boolean isValidCourseTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return false;
        }
        String trimmed = title.trim();
        return trimmed.length() >= 2 && trimmed.length() <= 100;
    }
}
