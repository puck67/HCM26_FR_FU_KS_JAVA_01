package utils;

import java.util.regex.Pattern;

public class Validator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\d{9,15}$");

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidScore(double score) {
        return score >= Constants.SCORE_MIN && score <= Constants.SCORE_MAX;
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidId(String id) {
        return id != null && !id.trim().isEmpty() && id.trim().length() <= 10;
    }

    public static boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        String cleanDate = dateStr.trim();
        if (cleanDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            try {
                java.time.LocalDate.parse(cleanDate);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        String replacedDate = cleanDate.replace('-', '/');
        try {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("d/M/yyyy")
                .withResolverStyle(java.time.format.ResolverStyle.SMART);
            java.time.LocalDate.parse(replacedDate, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String normalizeDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return "";
        }
        String cleanDate = dateStr.trim();
        if (cleanDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            try {
                java.time.LocalDate date = java.time.LocalDate.parse(cleanDate);
                return date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (Exception e) {
            }
        }
        String replacedDate = cleanDate.replace('-', '/');
        try {
            java.time.format.DateTimeFormatter inputFormatter = java.time.format.DateTimeFormatter.ofPattern("d/M/yyyy");
            java.time.LocalDate date = java.time.LocalDate.parse(replacedDate, inputFormatter);
            java.time.format.DateTimeFormatter outputFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return date.format(outputFormatter);
        } catch (Exception e) {
            return dateStr;
        }
    }

    public static boolean isValidCertificateNumber(String number) {
        if (number == null || number.trim().isEmpty()) {
            return false;
        }
        return number.trim().matches("^\\d+$");
    }
}
