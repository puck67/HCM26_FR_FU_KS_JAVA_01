package com.example.validation;

import java.util.regex.Pattern;

public class Validator {

    private static final Pattern NAME_PATTERN = Pattern.compile(
            "^[a-zA-ZÃƒâ‚¬ÃƒÂÃƒâ€šÃƒÆ’ÃƒË†Ãƒâ€°ÃƒÅ ÃƒÅ’ÃƒÂÃƒâ€™Ãƒâ€œÃƒâ€Ãƒâ€¢Ãƒâ„¢ÃƒÅ¡Ã„â€šÃ„ÂÃ„Â¨Ã…Â¨Ã†Â ÃƒÂ ÃƒÂ¡ÃƒÂ¢ÃƒÂ£ÃƒÂ¨ÃƒÂ©ÃƒÂªÃƒÂ¬ÃƒÂ­ÃƒÂ²ÃƒÂ³ÃƒÂ´ÃƒÂµÃƒÂ¹ÃƒÂºÃ„Æ’Ã„â€˜Ã„Â©Ã…Â©Ã†Â¡Ã†Â¯Ã„â€šÃƒâ€šÃƒÅ Ãƒâ€Ã†Â Ã¡Â»Â¨Ã¡Â»ÂªÃ¡Â»Â¬Ã¡Â»Â®Ã¡Â»Â°Ã¡ÂºÂ¤Ã¡ÂºÂ¦Ã¡ÂºÂ¨Ã¡ÂºÂªÃ¡ÂºÂ¬Ã¡ÂºÂ¤Ã¡ÂºÂ¦Ã¡ÂºÂ¨Ã¡ÂºÂªÃ¡ÂºÂ¬Ã¡ÂºÂ®Ã¡ÂºÂ°Ã¡ÂºÂ²Ã¡ÂºÂ´Ã¡ÂºÂ¶Ã¡ÂºÂ¸Ã¡ÂºÂºÃ¡ÂºÂ¼Ã¡Â»â‚¬Ã¡Â»â‚¬Ã¡Â»â€šÃ†Â°Ã„Æ’ÃƒÂ¢ÃƒÂªÃƒÂ´Ã†Â¡Ã¡Â»Â©Ã¡Â»Â«Ã¡Â»Â­Ã¡Â»Â¯Ã¡Â»Â±Ã¡ÂºÂ¥Ã¡ÂºÂ§Ã¡ÂºÂ©Ã¡ÂºÂ«Ã¡ÂºÂ­Ã¡ÂºÂ¯Ã¡ÂºÂ±Ã¡ÂºÂ³Ã¡ÂºÂµÃ¡ÂºÂ·Ã¡ÂºÂ¹Ã¡ÂºÂ»Ã¡ÂºÂ½Ã¡Â»ÂÃ¡Â»ÂÃ¡Â»Æ’Ã¡ÂºÂ¿Ã¡Â»â€¡Ã¡Â»â€°Ã¡Â»â€¹Ã¡Â»ÂÃ¡Â»ÂÃ¡Â»â€˜Ã¡Â»â€œÃ¡Â»â€¢Ã¡Â»â€”Ã¡Â»â„¢Ã¡Â»â€ºÃ¡Â»ÂÃ¡Â»Å¸Ã¡Â»Â¡Ã¡Â»Â£Ã¡Â»Â¥Ã¡Â»Â§Ã¡Â»Â©Ã¡Â»Â«Ã¡Â»Â­Ã¡Â»Â¯Ã¡Â»Â±Ã¡Â»Â³ÃƒÂ½Ã¡Â»ÂµÃ¡Â»Â·Ã¡Â»Â¹\\s]+$"
    );

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && NAME_PATTERN.matcher(name.trim()).matches() && name.length() <= 100;
    }

    public static boolean isValidAge(int age) {
        return age > 0 && age < 150;
    }

    public static boolean isValidCredit(int credit) {
        return credit > 0;
    }

    public static boolean isValidTitle(String title) {
        return title != null && !title.trim().isEmpty() && title.length() <= 100;
    }

    public static void validateStudent(String name, int age) {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Student name cannot be empty, contain numbers or special characters, and must be under 100 characters.");
        }
        if (!isValidAge(age)) {
            throw new IllegalArgumentException("Student age must be between 1 and 150.");
        }
    }

    public static void validateCourse(String title, int credit) {
        if (!isValidTitle(title)) {
            throw new IllegalArgumentException("Course title cannot be empty and must be under 100 characters.");
        }
        if (!isValidCredit(credit)) {
            throw new IllegalArgumentException("Course credit must be greater than 0.");
        }
    }
}
