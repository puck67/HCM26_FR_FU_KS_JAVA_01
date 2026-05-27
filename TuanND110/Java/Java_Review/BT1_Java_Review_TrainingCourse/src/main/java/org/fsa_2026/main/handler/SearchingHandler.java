package org.fsa_2026.main.handler;

import org.fsa_2026.enities.Cousrse;
import org.fsa_2026.enities.Learner;
import org.fsa_2026.enities.Trainner;
import org.fsa_2026.main.menu.TypingInput;
import org.fsa_2026.services.SearchingRecordService;

import java.util.Scanner;

public class SearchingHandler {
    private final SearchingRecordService searchingService;

    public SearchingHandler(SearchingRecordService searchingService) {
        this.searchingService = searchingService;
    }

    public void searchCourse(Scanner scanner) {
        System.out.println("--- Search Course ---");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        String choice = TypingInput.getInput("Choose search type: ", scanner);

        if (choice.equals("1")) {
            int id = TypingInput.getNumberInput("Enter course ID: ", scanner);
            Cousrse course = searchingService.findCousrseByID(id);
            if (course != null) {
                System.out.println("Course found: " + course);
            } else {
                System.out.println("Course not found.");
            }
        } else if (choice.equals("2")) {
            String name = TypingInput.getInput("Enter course name: ", scanner);
            Cousrse course = searchingService.findCousrseByName(name);
            if (course != null) {
                System.out.println("Course found: " + course);
            } else {
                System.out.println("Course not found.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    public void searchLearner(Scanner scanner) {
        System.out.println("--- Search Learner ---");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        String choice = TypingInput.getInput("Choose search type: ", scanner);

        if (choice.equals("1")) {
            int id = TypingInput.getNumberInput("Enter learner ID: ", scanner);
            Learner learner = searchingService.findLearnerByID(id);
            if (learner != null) {
                System.out.println("Learner found: " + learner);
            } else {
                System.out.println("Learner not found.");
            }
        } else if (choice.equals("2")) {
            String name = TypingInput.getInput("Enter learner name: ", scanner);
            Learner learner = searchingService.findLearnerByName(name);
            if (learner != null) {
                System.out.println("Learner found: " + learner);
            } else {
                System.out.println("Learner not found.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    public void searchTrainer(Scanner scanner) {
        System.out.println("--- Search Trainer ---");
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        String choice = TypingInput.getInput("Choose search type: ", scanner);

        if (choice.equals("1")) {
            int id = TypingInput.getNumberInput("Enter trainer ID: ", scanner);
            Trainner trainer = searchingService.findTrainnerByID(id);
            if (trainer != null) {
                System.out.println("Trainer found: " + trainer);
            } else {
                System.out.println("Trainer not found.");
            }
        } else if (choice.equals("2")) {
            String name = TypingInput.getInput("Enter trainer name: ", scanner);
            Trainner trainer = searchingService.findTrainnerByName(name);
            if (trainer != null) {
                System.out.println("Trainer found: " + trainer);
            } else {
                System.out.println("Trainer not found.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }
}
