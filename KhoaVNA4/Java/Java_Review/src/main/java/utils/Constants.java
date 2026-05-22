package utils;

import java.util.regex.Pattern;

public class Constants {
    public static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{9,10}$");

    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    public static final Pattern BIRTH_DATE_PATTERN = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");
}
