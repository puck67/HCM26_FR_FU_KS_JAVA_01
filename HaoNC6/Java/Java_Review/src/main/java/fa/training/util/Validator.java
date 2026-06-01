package fa.training.util;

import java.util.regex.Pattern;

public final class Validator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\d{10,11}$");

    private Validator() {
    }

    public static boolean isValidId(String id) {
        return id != null
                && !id.isBlank()
                && id.length() <= 10
                && id.matches("^[a-zA-Z0-9-]+$");
    }

    public static boolean isValidModel(String model) {
        return model != null && !model.isBlank();
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isBlank() && name.trim().length() <= 100;
    }

    public static boolean isValidCountry(String country) {
        return country != null && !country.trim().isBlank() && country.trim().length() <= 50;
    }

    public static boolean isValidDescription(String description) {
        return description == null || description.trim().length() <= 255;
    }

    public static boolean isValidPrice(double price) {
        return price > 0;
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }
}
