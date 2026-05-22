package org.fsa_2026.main;

import org.fsa_2026.main.handler.*;
import org.fsa_2026.main.menu.DisplayMenu;
import org.fsa_2026.main.menu.MenuController;
import org.fsa_2026.main.menu.TypingInput;
import org.fsa_2026.services.*;

import java.util.Scanner;

public class App {
    // Shared service instances
    private static final AddingRecordService addingService = new AddingRecordService();
    private static final SearchingRecordService searchingService = new SearchingRecordService();
    private static final UpdatingRecordService updatingService = new UpdatingRecordService();
    private static final DeletingRecordService deletingService = new DeletingRecordService();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        MenuController menuController = new MenuController(
                scanner,
                new AddingHandler(addingService),
                new SearchingHandler(searchingService),
                new UpdatingHandler(searchingService, updatingService),
                new DeletingHandler(deletingService),
                new InformationHandler(addingService)
        );

        do {
            System.out.println("===========Menu===========");
            DisplayMenu.displayMenu();
            System.out.println("==========================");
            String input = TypingInput.getInput("Enter your choice: ", scanner);
            System.out.println("==========================");
            switch (input) {
                case "1":
                    menuController.handleAddingSubmenu();
                    break;
                case "2":
                    menuController.handleSearchingSubmenu();
                    break;
                case "3":
                    menuController.handleUpdatingSubmenu();
                    break;
                case "4":
                    menuController.handleDeletingSubmenu();
                    break;
                case "5":
                    menuController.handleInformationSubmenu();
                    break;
                case "6":
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (true);
    }
}
