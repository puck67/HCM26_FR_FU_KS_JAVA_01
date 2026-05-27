package com.example.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class ConsoleMenu {
    private final String title;

    private final Map<String, Runnable> options = new LinkedHashMap<>();
    private final Scanner scanner = new Scanner(System.in);

    public ConsoleMenu(String title) {
        this.title = title;
    }

    public void addOption(String description, Runnable action) {
        options.put(description, action);
    }

    public void display() {
        while (true) {
            System.out.println("\n=== " + title.toUpperCase() + " ===");
            int index = 1;
            for (String desc : options.keySet()) {
                System.out.println(index++ + ". " + desc);
            }
            System.out.println("0. Thoát");
            System.out.print("Chọn chức năng (0-" + options.size() + "): ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice == 0) {
                    System.out.println("Đã thoát chương trình!");
                    break;
                }
                if (choice > 0 && choice <= options.size()) {
                    Runnable action = options.values().toArray(new Runnable[0])[choice - 1];
                    action.run();
                } else {
                    System.out.println("Lựa chọn không hợp lệ!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số!");
            }
        }
    }
}