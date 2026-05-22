package util;

import java.util.regex.Pattern;

public class Validator {
    private static final String ID_REGEX = "^B\\d{4}$";

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    private static final String PHONE_REGEX = "^\\d{10,11}$";

    public static boolean isValidBookId(String id) {
        return id != null && Pattern.matches(ID_REGEX, id);
    }

    public static boolean isValidEmail(String email) {
        return email != null && Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && Pattern.matches(PHONE_REGEX, phone);
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidPrice(double price) {
        return price > 0;
    }

    public static boolean isValidQuantity(int quantity) {
        return quantity >= 0;
    }
}