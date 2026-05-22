package fa.training.utils;

public class ValidationUtils {

    public static boolean isValidPrice(
            double price
    ) {

        return price > 0;
    }

    public static boolean isValidQuantity(
            int quantity
    ) {

        return quantity >= 0;
    }
}