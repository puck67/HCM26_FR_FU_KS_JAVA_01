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
}
