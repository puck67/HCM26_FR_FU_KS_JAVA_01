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
        int id = TypingInput.getNumberInput("Enter course ID: ", scanner);
        Cousrse course = searchingService.findCousrseByID(id);
        if (course != null) {
            System.out.println("Course found: " + course);
        } else {
            System.out.println("Course not found.");
        }
    }

    public void searchLearner(Scanner scanner) {
        System.out.println("--- Search Learner ---");
        int id = TypingInput.getNumberInput("Enter learner ID: ", scanner);
        Learner learner = searchingService.findLearnerByID(id);
        if (learner != null) {
            System.out.println("Learner found: " + learner);
        } else {
            System.out.println("Learner not found.");
        }
    }

    public void searchTrainer(Scanner scanner) {
        System.out.println("--- Search Trainer ---");
        int id = TypingInput.getNumberInput("Enter trainer ID: ", scanner);
        Trainner trainer = searchingService.findTrainnerByID(id);
        if (trainer != null) {
            System.out.println("Trainer found: " + trainer);
        } else {
            System.out.println("Trainer not found.");
        }
    }
}
