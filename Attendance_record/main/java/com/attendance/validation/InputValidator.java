package com.attendance.validation;

public final class InputValidator {
    private static final String EMPLOYEE_ID_REGEX = "^EMP\\d{3,}$";
    private static final String NAME_REGEX = "^[\\p{L} .'-]{2,100}$";
    private static final String TEXT_REGEX = "^[\\p{L}\\d .&()/-]{2,100}$";

    private InputValidator() {}

    public static boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    public static boolean isValidEmployeeId(String value) {
        return isNotBlank(value) && value.matches(EMPLOYEE_ID_REGEX);
    }

    public static boolean isValidName(String value) {
        return isNotBlank(value) && value.matches(NAME_REGEX);
    }

    public static boolean isValidText(String value) {
        return isNotBlank(value) && value.matches(TEXT_REGEX);
    }
}
