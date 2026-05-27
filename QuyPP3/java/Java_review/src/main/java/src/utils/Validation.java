package src.utils;

import java.util.List;

public class Validation {

    public static boolean isNotEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static boolean isPositiveDouble(String s) {
        try {
            return Double.parseDouble(s) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isNonNegativeInt(String s) {
        try {
            return Integer.parseInt(s) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isUniqueId(String id, List<String> list) {
        return !list.contains(id);
    }

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$");
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null) return false;
        return phone.matches("^\\d{10,11}$");
    }
}

