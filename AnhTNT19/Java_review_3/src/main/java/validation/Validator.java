package validation;

public class Validator {

    /** Not null and not blank */
    public static boolean isNotEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    /** difficulty must be 1–5 */
    public static boolean isValidDifficulty(int d) {
        return d >= 1 && d <= 5;
    }

    /** ID: alphanumeric, 2–10 chars */
    public static boolean isValidIdFormat(String id) {
        return id != null && id.matches("[A-Za-z0-9]{2,10}");
    }

    /**
     * Parse int safely; returns -1 on failure.
     * Caller should check isValidDifficulty() after.
     */
    public static int parseIntSafe(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
