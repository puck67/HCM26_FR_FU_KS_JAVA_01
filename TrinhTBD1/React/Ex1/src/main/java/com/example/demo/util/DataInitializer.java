package com.example.demo.util;

import com.example.demo.model.Student;
import com.example.demo.model.Task;
import com.example.demo.model.Teacher;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.repository.TeacherRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final TaskRepository taskRepository;

    public DataInitializer(TeacherRepository teacherRepository, StudentRepository studentRepository, TaskRepository taskRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) {
        if (teacherRepository.count() == 0) {
            teacherRepository.save(new Teacher(null, "Alice Smith", "alice.smith@example.com", "Physics", 5500.0));
            teacherRepository.save(new Teacher(null, "Bob Johnson", "bob.johnson@example.com", "Mathematics", 6000.0));
            teacherRepository.save(new Teacher(null, "Charlie Brown", "charlie.brown@example.com", "Chemistry", 5200.0));
        }

        if (studentRepository.count() == 0) {
            studentRepository.save(new Student(null, "David Lee", "david.lee@example.com", 8.5, "Class-12A"));
            studentRepository.save(new Student(null, "Emma Watson", "emma.watson@example.com", 9.2, "Class-12B"));
            studentRepository.save(new Student(null, "Frank Miller", "frank.miller@example.com", 7.8, "Class-11A"));
        }

        if (taskRepository.count() == 0) {
            taskRepository.save(new Task(null, "Setup project structure", "Initialize Spring Boot project with dependencies", "Done", 1));
            taskRepository.save(new Task(null, "Implement CRUD operations", "Build generic CRUD for all entities", "In Progress", 2));
            taskRepository.save(new Task(null, "Write unit tests", "Create comprehensive test suite", "Todo", 3));
        }
    }
}
