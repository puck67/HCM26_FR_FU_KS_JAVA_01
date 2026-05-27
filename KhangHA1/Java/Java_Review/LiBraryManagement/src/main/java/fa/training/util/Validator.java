package fa.training.util;

public class Validator {

    private Validator() {
    }

    // =========================
    // BOOK ID
    // Example: B001
    // =========================

    public static boolean isValidBookId(
            String id
    ) {

        return id != null
                && id.matches("B\\d{3}");
    }

    // =========================
    // NAME
    // =========================

    public static boolean isValidName(
            String name
    ) {

        return name != null
                && !name.trim().isEmpty()
                && name.length() <= 100;
    }

    // =========================
    // TITLE
    // =========================

    public static boolean isValidTitle(
            String title
    ) {

        return title != null
                && !title.trim().isEmpty()
                && title.length() <= 200;
    }

    // =========================
    // PRICE
    // =========================

    public static boolean isValidPrice(
            double price
    ) {

        return price > 0;
    }

    // =========================
    // PUBLISH YEAR
    // =========================

    public static boolean isValidPublishYear(
            int year
    ) {

        return year >= 1900
                && year <= 2100;
    }

    // =========================
    // AUTHOR ID
    // =========================

    public static boolean isValidAuthorId(
            Integer id
    ) {

        return id != null && id > 0;
    }

    // =========================
    // CATEGORY ID
    // =========================

    public static boolean isValidCategoryId(
            Integer id
    ) {

        return id != null && id > 0;
    }
}