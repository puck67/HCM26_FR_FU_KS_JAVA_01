package org.fsa_2026.main.handler;

import org.fsa_2026.enities.Cousrse;
import org.fsa_2026.enities.Learner;
import org.fsa_2026.enities.Trainner;
import org.fsa_2026.services.AddingRecordService;

import java.util.Set;

public class InformationHandler {
    private final AddingRecordService addingService;

    public InformationHandler(AddingRecordService addingService) {
        this.addingService = addingService;
    }

    public void displayAllCourses() {
        System.out.println("--- All Courses ---");
        Set<Cousrse> courses = addingService.getCousrses();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            courses.forEach(System.out::println);
        }
    }

    public void displayAllLearners() {
        System.out.println("--- All Learners ---");
        Set<Learner> learners = addingService.getLearners();
        if (learners.isEmpty()) {
            System.out.println("No learners found.");
        } else {
            learners.forEach(System.out::println);
        }
    }

    public void displayAllTrainers() {
        System.out.println("--- All Trainers ---");
        Set<Trainner> trainers = addingService.getTrainners();
        if (trainers.isEmpty()) {
            System.out.println("No trainers found.");
        } else {
            trainers.forEach(System.out::println);
        }
    }
}

