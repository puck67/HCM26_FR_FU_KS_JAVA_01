package org.fsa_2026.main.menu;

import org.fsa_2026.main.handler.AddingHandler;
import org.fsa_2026.main.handler.DeletingHandler;
import org.fsa_2026.main.handler.InformationHandler;
import org.fsa_2026.main.handler.SearchingHandler;
import org.fsa_2026.main.handler.UpdatingHandler;

import java.util.Scanner;

public class MenuController {
    private final Scanner scanner;
    private final AddingHandler addingHandler;
    private final SearchingHandler searchingHandler;
    private final UpdatingHandler updatingHandler;
    private final DeletingHandler deletingHandler;
    private final InformationHandler informationHandler;

    public MenuController(
            Scanner scanner,
            AddingHandler addingHandler,
            SearchingHandler searchingHandler,
            UpdatingHandler updatingHandler,
            DeletingHandler deletingHandler,
            InformationHandler informationHandler
    ) {
        this.scanner = scanner;
        this.addingHandler = addingHandler;
        this.searchingHandler = searchingHandler;
        this.updatingHandler = updatingHandler;
        this.deletingHandler = deletingHandler;
        this.informationHandler = informationHandler;
    }

    public void handleAddingSubmenu() {
        DisplayMenu.displayMenuAdding();
        System.out.println("==========================");
        String addChoice = TypingInput.getInput("Enter your choice: ", scanner);
        switch (addChoice) {
            case "1":
                addingHandler.addNewCourse(scanner);
                break;
            case "2":
                addingHandler.addNewLearner(scanner);
                break;
            case "3":
                addingHandler.addNewTrainer(scanner);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        System.out.println("==========================");
    }

    public void handleSearchingSubmenu() {
        DisplayMenu.displayMenuSearching();
        System.out.println("==========================");
        String searchChoice = TypingInput.getInput("Enter your choice: ", scanner);
        switch (searchChoice) {
            case "1":
                searchingHandler.searchCourse(scanner);
                break;
            case "2":
                searchingHandler.searchLearner(scanner);
                break;
            case "3":
                searchingHandler.searchTrainer(scanner);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        System.out.println("==========================");
    }

    public void handleUpdatingSubmenu() {
        DisplayMenu.displayMenuUpdating();
        System.out.println("==========================");
        String updateChoice = TypingInput.getInput("Enter your choice: ", scanner);
        switch (updateChoice) {
            case "1":
                updatingHandler.updateCourse(scanner);
                break;
            case "2":
                updatingHandler.updateLearner(scanner);
                break;
            case "3":
                updatingHandler.updateTrainer(scanner);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        System.out.println("==========================");
    }

    public void handleDeletingSubmenu() {
        DisplayMenu.displayMenuDeleting();
        System.out.println("==========================");
        String deleteChoice = TypingInput.getInput("Enter your choice: ", scanner);
        switch (deleteChoice) {
            case "1":
                deletingHandler.deleteCourse(scanner);
                break;
            case "2":
                deletingHandler.deleteLearner(scanner);
                break;
            case "3":
                deletingHandler.deleteTrainer(scanner);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        System.out.println("==========================");
    }

    public void handleInformationSubmenu() {
        DisplayMenu.displayInformation();
        System.out.println("==========================");
        String infoChoice = TypingInput.getInput("Enter your choice: ", scanner);
        switch (infoChoice) {
            case "1":
                informationHandler.displayAllCourses();
                break;
            case "2":
                informationHandler.displayAllLearners();
                break;
            case "3":
                informationHandler.displayAllTrainers();
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
        System.out.println("==========================");
    }
}

