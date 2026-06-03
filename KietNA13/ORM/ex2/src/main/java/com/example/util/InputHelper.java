package com.example.util;

import java.util.Scanner;

public class InputHelper {

    private static final String INVALID_NUMBER_MSG = "  [!] Vui lòng nhập số nguyên hợp lệ.";
    private static final String BLANK_INPUT_MSG    = "  [!] Không được để trống.";
    private static final String OUT_OF_RANGE_MSG   = "  [!] Giá trị phải từ %d đến %d.";
    private static final String POSITIVE_ONLY_MSG  = "  [!] Giá trị phải lớn hơn 0.";

    public static int readInt(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String raw = sc.nextLine().trim();
            try {
                return Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                System.out.println(INVALID_NUMBER_MSG);
            }
        }
    }

    public static int readPositiveInt(Scanner sc, String prompt) {
        while (true) {
            int value = readInt(sc, prompt);
            if (value > 0) return value;
            System.out.println(POSITIVE_ONLY_MSG);
        }
    }

    public static int readIntInRange(Scanner sc, String prompt, int min, int max) {
        while (true) {
            int value = readInt(sc, prompt);
            if (value >= min && value <= max) return value;
            System.out.printf(OUT_OF_RANGE_MSG + "%n", min, max);
        }
    }

    public static String readString(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = sc.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println(BLANK_INPUT_MSG);
        }
    }
}
