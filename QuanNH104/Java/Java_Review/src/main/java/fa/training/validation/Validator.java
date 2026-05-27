package fa.training.validation;

import java.util.regex.Pattern;

public class Validator {
    // Regex for IDs
    private static final Pattern WAREHOUSE_ID_PATTERN = Pattern.compile("^WH-\\d{1,7}$");
    private static final Pattern EMPLOYEE_ID_PATTERN = Pattern.compile("^EM-\\d{1,7}$");
    private static final Pattern PRODUCT_ID_PATTERN = Pattern.compile("^PR-\\d{1,7}$");

    // Regex for standard RFC 5322 email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    // Regex for phone number (10 to 11 digits, starting with 0)
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0\\d{9,10}$");

    // Regex for names (only letters, spaces, and Vietnamese characters)
    private static final Pattern NAME_PATTERN = Pattern.compile(
            "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂÂÊÔƠỨỪỬỮỰẤẦẨẪẬẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăâêôơứừửữựấầẩẫậắằẳẵặẹẻẽềềểếệỉịọỏốồổỗộớờởỡợụủứừửữựỳýỵỷỹ\\s]+$"
    );

    public static boolean isValidWarehouseId(String id) {
        return id != null && WAREHOUSE_ID_PATTERN.matcher(id.trim()).matches();
    }

    public static boolean isValidEmployeeId(String id) {
        return id != null && EMPLOYEE_ID_PATTERN.matcher(id.trim()).matches();
    }

    public static boolean isValidProductId(String id) {
        return id != null && PRODUCT_ID_PATTERN.matcher(id.trim()).matches();
    }

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && NAME_PATTERN.matcher(name.trim()).matches() && name.length() <= 100;
    }

    public static boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty() && address.length() >= 5 && address.length() <= 200;
    }

    public static boolean isValidCapacity(int capacity) {
        return capacity > 0;
    }

    public static boolean isValidPrice(double price) {
        return price > 0;
    }

    public static boolean isValidQuantity(int quantity) {
        return quantity >= 0;
    }

    public static void validateWarehouse(String id, String name, String address, int capacity) {
        if (!isValidWarehouseId(id)) {
            throw new IllegalArgumentException("Invalid Warehouse ID! Format must be WH-XXX (e.g., WH-001) and maximum 10 characters.");
        }
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Warehouse name cannot be empty, contain special characters, or exceed 100 characters.");
        }
        if (!isValidAddress(address)) {
            throw new IllegalArgumentException("Warehouse address must be between 5 and 200 characters.");
        }
        if (!isValidCapacity(capacity)) {
            throw new IllegalArgumentException("Warehouse capacity must be greater than 0.");
        }
    }

    public static void validateEmployee(String id, String name, String email, String phone) {
        if (!isValidEmployeeId(id)) {
            throw new IllegalArgumentException("Invalid Employee ID! Format must be EM-XXX (e.g., EM-001) and maximum 10 characters.");
        }
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Employee name cannot be empty, contain numbers or special characters, and must be under 100 characters.");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format (e.g., employee@domain.com).");
        }
        if (!isValidPhone(phone)) {
            throw new IllegalArgumentException("Invalid phone number! It must start with 0, contain only digits, and have a length of 10 to 11 digits.");
        }
    }

    public static void validateProduct(String id, String name, double price, int quantity) {
        if (!isValidProductId(id)) {
            throw new IllegalArgumentException("Invalid Product ID! Format must be PR-XXX (e.g., PR-001) and maximum 10 characters.");
        }
        if (name == null || name.trim().isEmpty() || name.length() > 100) {
            throw new IllegalArgumentException("Product name cannot be empty and must be under 100 characters.");
        }
        if (!isValidPrice(price)) {
            throw new IllegalArgumentException("Product price must be greater than 0.");
        }
        if (!isValidQuantity(quantity)) {
            throw new IllegalArgumentException("Product quantity cannot be negative.");
        }
    }
}
