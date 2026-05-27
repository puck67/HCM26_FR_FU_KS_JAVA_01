package org.fsa_2026.main.handler;

import org.fsa_2026.enities.Cousrse;
import org.fsa_2026.enities.Learner;
import org.fsa_2026.enities.Trainner;
import org.fsa_2026.main.menu.TypingInput;
import org.fsa_2026.services.SearchingRecordService;
import org.fsa_2026.services.UpdatingRecordService;

import java.util.Scanner;

public class UpdatingHandler {
    private final SearchingRecordService searchingService;
    private final UpdatingRecordService updatingService;

    public UpdatingHandler(SearchingRecordService searchingService, UpdatingRecordService updatingService) {
        this.searchingService = searchingService;
        this.updatingService = updatingService;
    }

    public void updateCourse(Scanner scanner) {
        System.out.println("--- Update Course ---");
        int id = TypingInput.getNumberInput("Enter course ID to update: ", scanner);
        String newName = TypingInput.getInput("Enter new course name: ", scanner);
        String newDescription = TypingInput.getInput("Enter new description: ", scanner);

        if (updatingService.updateCousrseName(id, newName)) {
            Cousrse course = searchingService.findCousrseByID(id);
            if (course != null) {
                course.setDescription(newDescription);
            }
            System.out.println("Course updated successfully!");
        } else {
            System.out.println("Course not found.");
        }
    }

    public void updateLearner(Scanner scanner) {
        System.out.println("--- Update Learner ---");
        int id = TypingInput.getNumberInput("Enter learner ID to update: ", scanner);
        String newName = TypingInput.getInput("Enter new learner name: ", scanner);
        String newPhone = TypingInput.getPhoneInput("Enter new phone: ", scanner);
        String newEmail = TypingInput.getEmailInput("Enter new email: ", scanner);

        if (updatingService.updateLearnerName(id, newName)) {
            Learner learner = searchingService.findLearnerByID(id);
            if (learner != null) {
                learner.setPhone(newPhone);
                learner.setEmail(newEmail);
            }
            System.out.println("Learner updated successfully!");
        } else {
            System.out.println("Learner not found.");
        }
    }

    public void updateTrainer(Scanner scanner) {
        System.out.println("--- Update Trainer ---");
        int id = TypingInput.getNumberInput("Enter trainer ID to update: ", scanner);
        String newName = TypingInput.getInput("Enter new trainer name: ", scanner);
        String newPhone = TypingInput.getPhoneInput("Enter new phone: ", scanner);
        String newEmail = TypingInput.getEmailInput("Enter new email: ", scanner);

        if (updatingService.updateTrainnerName(id, newName)) {
            Trainner trainer = searchingService.findTrainnerByID(id);
            if (trainer != null) {
                trainer.setPhone(newPhone);
                trainer.setEmail(newEmail);
            }
            System.out.println("Trainer updated successfully!");
        } else {
            System.out.println("Trainer not found.");
        }
    }
}

