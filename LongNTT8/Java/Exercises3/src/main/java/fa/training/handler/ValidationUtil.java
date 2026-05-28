package fa.training.handler;

import java.util.UUID;
import java.util.regex.Pattern;

public class ValidationUtil {

    public static String generateId(String prefix) {
        String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return prefix + "-" + randomPart;
    }

    public static boolean isValidId(String id, String prefix) {
        if (id == null || id.isEmpty())
            return false;
        String regex = "^" + prefix + "-[A-Z0-9]{8}$";
        return Pattern.matches(regex, id);
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty())
            return false;
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        return Pattern.matches(emailRegex, email);
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty())
            return false;
        // Số điện thoại Việt Nam bắt đầu bằng số 0, theo sau là 9 chữ số
        String phoneRegex = "^0\\d{9}$";
        return Pattern.matches(phoneRegex, phone);
    }
}
