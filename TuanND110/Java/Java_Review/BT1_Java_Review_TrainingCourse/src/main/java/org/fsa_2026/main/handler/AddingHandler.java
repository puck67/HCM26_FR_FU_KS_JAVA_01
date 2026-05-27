package org.fsa_2026.main.handler;

import org.fsa_2026.enities.Cousrse;
import org.fsa_2026.enities.Learner;
import org.fsa_2026.enities.Trainner;
import org.fsa_2026.main.menu.TypingInput;
import org.fsa_2026.services.AddingRecordService;

import java.util.Date;
import java.util.Scanner;

public class AddingHandler {
    private final AddingRecordService addingService;

    public AddingHandler(AddingRecordService addingService) {
        this.addingService = addingService;
    }

    public void addNewCourse(Scanner scanner) {
        System.out.println("--- Add New Course ---");
        String courseName = TypingInput.getInput("Enter course name: ", scanner);
        String description = TypingInput.getInput("Enter description: ", scanner);
        Date startDate = TypingInput.getDateInput("Enter start date (yyyy-MM-dd): ", scanner);
        Date endDate = TypingInput.getDateInput("Enter end date (yyyy-MM-dd): ", scanner);

        Cousrse course = new Cousrse();
        course.setCousrseName(courseName);
        course.setDescription(description);
        course.setStartTime(startDate);
        course.setEndTime(endDate);

        addingService.addingNewCousrse(course);
        System.out.println("Course added successfully!");
    }

    public void addNewLearner(Scanner scanner) {
        System.out.println("--- Add New Learner ---");
        String learnerName = TypingInput.getInput("Enter learner name: ", scanner);
        String phone = TypingInput.getPhoneInput("Enter phone: ", scanner);
        String email = TypingInput.getEmailInput("Enter email: ", scanner);
        Date birthDate = TypingInput.getDateInput("Enter birth date (yyyy-MM-dd): ", scanner);
        String classroom = TypingInput.getInput("Enter classroom: ", scanner);

        Learner learner = new Learner();
        learner.setStudentName(learnerName);
        learner.setPhone(phone);
        learner.setEmail(email);
        learner.setBirthDate(birthDate);
        learner.setClassRoom(classroom);

        addingService.addingNewLearner(learner);
        System.out.println("Learner added successfully!");
    }

    public void addNewTrainer(Scanner scanner) {
        System.out.println("--- Add New Trainer ---");
        String trainerName = TypingInput.getInput("Enter trainer name: ", scanner);
        String phone = TypingInput.getPhoneInput("Enter phone: ", scanner);
        String email = TypingInput.getEmailInput("Enter email: ", scanner);
        Date birthDate = TypingInput.getDateInput("Enter birth date (yyyy-MM-dd): ", scanner);
        String className = TypingInput.getInput("Enter class name: ", scanner);

        Trainner trainer = new Trainner();
        trainer.setTrainnerName(trainerName);
        trainer.setPhone(phone);
        trainer.setEmail(email);
        trainer.setBirthDate(birthDate);
        trainer.setClassName(className);

        addingService.addingNewTrainner(trainer);
        System.out.println("Trainer added successfully!");
    }
}

