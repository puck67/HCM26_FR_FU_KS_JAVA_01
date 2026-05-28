package com.example.validation;

import java.util.regex.Pattern;

public class Validator {

    private static final Pattern NAME_PATTERN = Pattern.compile(
            "^[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚÝàáâãèéêìíòóôõùúýĂăĐđĨĩŨũƠơƯưẠạẢảẤấẦầẨẩẪẫẬậẮắẰằẲẳẴẵẶặẸẹẺẻẼẽẾếỀềỂểỄễỆệỈỉỊịỌọỎỏỐốỒồỔổỖỗỘộỚớỜờỞởỠỡỢợỤụỦủỨứỪừỬửỮữỰựỲỳỶỷỸỹỹ\\s]+$"
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
