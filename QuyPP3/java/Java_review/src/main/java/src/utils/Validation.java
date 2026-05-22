package src.utils;

import src.entities.Product;

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
}
