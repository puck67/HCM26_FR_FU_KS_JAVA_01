package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ValidationUtils {

    public static boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isAfter(String startStr, String endStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            java.util.Date start = sdf.parse(startStr);
            java.util.Date end = sdf.parse(endStr);
            return end.after(start);
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isNotEmpty(String input) {
        return input != null && !input.trim().isEmpty();
    }

    public static boolean isDifferent(String p1, String p2) {
        return p1 != null && !p1.equalsIgnoreCase(p2);
    }
}
