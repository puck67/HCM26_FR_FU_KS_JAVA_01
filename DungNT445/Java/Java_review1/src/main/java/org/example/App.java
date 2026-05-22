package org.example;

import org.example.config.DatabaseConfig;
import org.example.ui.ConsoleMenu;

public class App {
    public static void main(String[] args) {
        System.out.println("Starting Student & Grade Management System...");

        // Setup H2 schema and seed initial sample data
        DatabaseConfig.initializeDatabase();

        // Launch Console UI Menu
        ConsoleMenu menu = new ConsoleMenu();
        menu.showMainMenu();
    }
}
