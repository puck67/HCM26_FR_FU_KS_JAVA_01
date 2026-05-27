package org.fsa_2026.util;

public class Validation {
    public static boolean isEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static boolean isPhone(String phone) {
        return phone != null && phone.matches("\\d{10}");
    }

    public static boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }

    public static boolean isEmty(String input) {
        return isEmpty(input);
    }

    public static boolean isNumber(String input) {
        if (input == null) {
            return false;
        }
        try {
            Integer.parseInt(input.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
