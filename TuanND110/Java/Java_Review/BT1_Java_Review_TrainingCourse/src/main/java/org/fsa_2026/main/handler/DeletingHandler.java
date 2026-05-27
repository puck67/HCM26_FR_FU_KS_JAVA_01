package org.fsa_2026.main.handler;

import org.fsa_2026.main.menu.TypingInput;
import org.fsa_2026.services.DeletingRecordService;

import java.util.Scanner;

public class DeletingHandler {
    private final DeletingRecordService deletingService;

    public DeletingHandler(DeletingRecordService deletingService) {
        this.deletingService = deletingService;
    }

    public void deleteCourse(Scanner scanner) {
        System.out.println("--- Delete Course ---");
        int id = TypingInput.getNumberInput("Enter course ID to delete: ", scanner);
        if (deletingService.deleteCousrseByID(id)) {
            System.out.println("Course deleted successfully!");
        } else {
            System.out.println("Course not found.");
        }
    }

    public void deleteLearner(Scanner scanner) {
        System.out.println("--- Delete Learner ---");
        int id = TypingInput.getNumberInput("Enter learner ID to delete: ", scanner);
        if (deletingService.deleteLearnerByID(id)) {
            System.out.println("Learner deleted successfully!");
        } else {
            System.out.println("Learner not found.");
        }
    }

    public void deleteTrainer(Scanner scanner) {
        System.out.println("--- Delete Trainer ---");
        int id = TypingInput.getNumberInput("Enter trainer ID to delete: ", scanner);
        if (deletingService.deleteTrainnerByID(id)) {
            System.out.println("Trainer deleted successfully!");
        } else {
            System.out.println("Trainer not found.");
        }
    }
}

